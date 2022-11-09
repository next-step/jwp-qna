package qna.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.question.Question;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;
import qna.domain.user.User;
import qna.domain.user.userid.UserId;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private UserRepository userRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, UserRepository userRepository,
                      DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(String userId, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        Optional<User> findUser = userRepository.findByUserId(new UserId(userId));
        deleteHistoryService.saveAll(question.delete(findUser.orElseThrow(NotFoundException::new)));
    }
}
