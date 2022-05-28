package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private UserRepository userRepository;
    private QuestionRepository questionRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(final UserRepository userRepository, QuestionRepository questionRepository,
                      DeleteHistoryService deleteHistoryService) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(Long loginUserId, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        User loginUser = findUserById(loginUserId);
        deleteHistoryService.saveAll(question.delete(loginUser).getList());
    }

    private User findUserById(final Long loginUserId) {
        return userRepository.findById(loginUserId).orElseThrow(NotFoundException::new);
    }

}
