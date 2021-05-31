package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

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
        Question question = findQuestionById(questionId);
        question.delete(loginUser);
        saveDeleteHistories(question, loginUser);
    }

    @Transactional
    public void saveDeleteHistories(Question question, User deleter) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleteHistories.add(DeleteHistory.createDeleteHistory(ContentType.QUESTION, question.getId(), deleter));
        question.getAnswers().forEach(answer ->
                deleteHistories.add(DeleteHistory.createDeleteHistory(ContentType.ANSWER, answer.getId(), deleter)));

        deleteHistoryService.saveAll(deleteHistories);
    }
}
