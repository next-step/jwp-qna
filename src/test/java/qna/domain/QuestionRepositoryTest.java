package qna.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    User testUser;

    @BeforeEach
    void beforeEach() {
        testUser = new User("test", "pw", "테스트유저", "a@naver.com");
        userRepository.save(testUser);
    }

    @DisplayName("Question 저장 테스트")
    @Test
    void saveTest() {
        Question expected = QuestionTest.Q1.writeBy(testUser);
        Question result = questionRepository.save(expected);

        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Question 조회 테스트 / id로 조회")
    @Test
    void findTest() {
        Question expected = questionRepository.save(QuestionTest.Q2.writeBy(testUser));
        Optional<Question> resultOptional = questionRepository.findByIdAndDeletedFalse(expected.getId());
        assertThat(resultOptional).isNotEmpty();

        Question result = resultOptional.get();

        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }
}