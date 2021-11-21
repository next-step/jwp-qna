package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.DeleteHistories;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
public class QnaService {

    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(
        final QuestionRepository questionRepository,
        final DeleteHistoryService deleteHistoryService
    ) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(
        final User loginUser,
        final Long questionId
    ) throws CannotDeleteException {
        final Question question = findQuestionById(questionId);
        final DeleteHistories deleteHistories = question.delete(loginUser);
        deleteHistoryService.saveAll(deleteHistories);
    }

    private Question findQuestionById(final Long id) {
        return questionRepository.findById(id)
            .orElseThrow(NotFoundException::new);
    }
}
