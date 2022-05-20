package qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Answers;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryContent;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository,
                      DeleteHistoryService deleteHistoryService) {
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
        Question question = findQuestionById(questionId);
        question.validateRemovable(loginUser);

        Answers answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        answers.validateExistOtherAnswer(loginUser);
        
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted(true);
        deleteHistories.add(
                new DeleteHistory(new DeleteHistoryContent(ContentType.QUESTION, questionId), question.getWriter(),
                        LocalDateTime.now()));
        for (Answer answer : answers.list()) {
            answer.setDeleted(true);
            deleteHistories.add(
                    new DeleteHistory(new DeleteHistoryContent(ContentType.ANSWER, answer.getId()), answer.getWriter(),
                            LocalDateTime.now()));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
