package qna;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaStudyTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("user 객체 저장후 반환되는 객체는 같다")
    void save1() {
        User user = new User("user1", "pw", "name", "mai@mail.com");
        User savedUser = userRepository.save(user);
        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("answer 저장하고 삭제한 객체를 다시 저장해 본다")
    void saveTest2() {
        User user = new User("user1", "pw", "name", "mai@mail.com");
        userRepository.save(user);
        userRepository.delete(user);
        userRepository.flush();
        userRepository.save(user);
    }

}
