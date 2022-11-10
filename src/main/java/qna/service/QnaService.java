package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.DateTimeStrategy;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private DeleteHistoryService deleteHistoryService;
    private UserRepository userRepository;
    private DateTimeStrategy dateTimeStrategy;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService, UserRepository userRepository, DateTimeStrategy dateTimeStrategy) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
        this.userRepository = userRepository;
        this.dateTimeStrategy = dateTimeStrategy;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(Long userId, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);

        User loginUser = userRepository.findById(userId)
            .orElseThrow(() -> new CannotDeleteException("유저를 찾을 수 없습니다."));
        deleteHistoryService.saveAll(question.delete(loginUser, dateTimeStrategy.getNowDateTime()));
    }
}
