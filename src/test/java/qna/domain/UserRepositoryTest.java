package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User testUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        testUser = new User("user", "1234", "username", "test@gmail.com");
        savedUser = userRepository.save(testUser);
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저를 저장한다.")
    void save() {
        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(testUser);
    }

    @Test
    @DisplayName("유저ID로 유저를 조회한다.")
    void findByUserId() {
        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());

        assertThat(foundUser)
                .isNotEmpty()
                .hasValueSatisfying(user -> assertThat(user).isEqualTo(savedUser));
    }

}
