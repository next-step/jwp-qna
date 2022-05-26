package qna.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private DeleteHistoryRepository deleteHistoryRepository;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryRepository deleteHistoryRepository) {
        this.questionRepository = questionRepository;
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        List<DeleteHistory> deleteHistories = question.delete(loginUser);
        deleteHistoryRepository.saveAll(deleteHistories);
    }
}
