package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private DeleteHistoryService deleteHistoryService;

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
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {

        Question question = findQuestionForDeleteWithSameWriterAuth(loginUser, questionId);
        List<Answer> answers = findAnswersForDeleteWithSameWriterAuth(loginUser, questionId);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted(true);
        deleteHistories.add( DeleteHistory.forQuestionOf(questionId, loginUser));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(DeleteHistory.forAnswerOf(answer.getId(), loginUser));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }

    public Question findQuestionForDeleteWithSameWriterAuth(User loginUser, Long questionId)  throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        return question;
    }

    public List<Answer> findAnswersForDeleteWithSameWriterAuth(User loginUser, Long questionId) throws CannotDeleteException {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
        return answers;
    }
}
