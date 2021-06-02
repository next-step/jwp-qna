package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
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
        question.validateIsOwner(loginUser);

        Answers answers = question.getAnswers();
        answers.validateOwners(loginUser);

        saveHistories(questionId, question, answers);
    }

    private void saveHistories(Long questionId, Question question, Answers answers) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, question.getWriter(), LocalDateTime.now()));
        answers.deleteAllAndAddHistories(deleteHistories);
        deleteHistoryService.saveAll(deleteHistories);
    }
}
