package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

    @Autowired
    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Question inputQuestion) throws CannotDeleteException {
        Question question = findQuestionById(inputQuestion.getId());
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(inputQuestion);
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, inputQuestion.getId(), question.getWriterId(), LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriterId(), LocalDateTime.now()));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
