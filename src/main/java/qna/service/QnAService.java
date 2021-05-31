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
public class QnAService {
    private static final Logger log = LoggerFactory.getLogger(QnAService.class);

    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnAService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
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
        DeleteHistories deleteHistories = question.deleteByOwner(loginUser);
        deleteHistoryService.saveAll(deleteHistories.getDeleteHistories());
    }
}
