package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.repository.entity.UserTest.JAVAJIGI;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(JAVAJIGI);
    }

    @Test()
    @DisplayName("유저를 ID로 찾는다")
    void findByUserId() {
        Optional<User> byUserId = userRepository.findByUserId(JAVAJIGI.getUserId());
        User user = byUserId.orElse(null);

        assertAll(
                () -> assertThat(byUserId).isNotEmpty(),
                () -> assertEquals(1, user.getId()),
                () -> assertEquals("javajigi", user.getUserId()),
                () -> assertEquals("name", user.getName()),
                () -> assertEquals("password", user.getPassword()),
                () -> assertEquals("javajigi@slipp.net", user.getEmail())
        );
    }
}
