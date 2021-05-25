package qna.domain.utils;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistoryRepository;
import qna.domain.QuestionRepository;
import qna.domain.UserRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public
class JpaTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    AnswerRepository answerRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    public DeleteHistoryRepository getDeleteHistoryRepository() {
        return deleteHistoryRepository;
    }

    public AnswerRepository getAnswerRepository() {
        return answerRepository;
    }
}
