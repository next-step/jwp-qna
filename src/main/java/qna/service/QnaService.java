package qna.service;

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

    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(final QuestionRepository questionRepository, final DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(final Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(final User loginUser, final Long questionId) throws CannotDeleteException {
        deleteHistoryService.saveAll(deleteQuestion(loginUser, findQuestionById(questionId)));
    }

    private DeleteHistories deleteQuestion(final User loginUser, final Question question) throws CannotDeleteException {
        return question.delete(loginUser);
    }
}
