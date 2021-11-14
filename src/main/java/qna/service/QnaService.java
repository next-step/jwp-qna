package qna.service;

import java.time.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import qna.*;
import qna.domain.*;

@Service
public class QnaService {
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
        DeleteHistories deleteHistories = question.delete(loginUser, LocalDateTime.now());
        deleteHistoryService.saveAll(deleteHistories.getDeleteHistories());
    }
}
