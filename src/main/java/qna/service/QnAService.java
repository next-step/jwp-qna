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
public class QnAService {

    private static final Logger log = LoggerFactory.getLogger(QnAService.class);

    public static final String MESSAGE_QUESTION_NOT_FOUND = "질문을 찾을 수 없습니다.";
    public static final String MESSAGE_USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";

    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;
    private final UserRepository userRepository;

    private final DateTimeStrategy dateTimeStrategy;

    public QnAService(QuestionRepository questionRepository,
                      DeleteHistoryService deleteHistoryService,
                      UserRepository userRepository,
                      DateTimeStrategy dateTimeStrategy) {

        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
        this.userRepository = userRepository;
        this.dateTimeStrategy = dateTimeStrategy;
    }

    @Transactional
    public void deleteQuestion(long loginUserId, long questionId) throws CannotDeleteException {

        Question question =
            questionRepository.findById(questionId)
                              .orElseThrow(() -> new CannotDeleteException(
                                  MESSAGE_QUESTION_NOT_FOUND));

        User loginUser =
            userRepository.findById(loginUserId)
                          .orElseThrow(() -> new NotFoundException(MESSAGE_USER_NOT_FOUND));

        deleteHistoryService.saveAll(question.delete(loginUser, dateTimeStrategy));
    }
}
