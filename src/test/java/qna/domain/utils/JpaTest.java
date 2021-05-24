package qna.domain.utils;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import qna.domain.QuestionRepository;
import qna.domain.UserRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public
class JpaTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }
}
