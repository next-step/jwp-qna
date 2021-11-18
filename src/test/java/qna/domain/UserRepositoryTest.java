package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @DisplayName("user userId로 찾기")
    @Test
    void findUserByIdTest() {
        assertThat(userRepository.findByUserId(new UserId("javajigi"))).isNotNull();
    }

    @DisplayName("user 삭제")
    @Test
    void removeUserTest() {
        assertThat(userRepository.findAll().size()).isEqualTo(1);
        userRepository.delete(user);
        assertThat(userRepository.findAll().size()).isZero();
    }

    @AfterEach
    void beforeFinish() {
        userRepository.flush();
    }

}
