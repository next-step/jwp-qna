package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(user());
    }

    @Test
    void 사용자를_저장한다() {
        assertAll(
                () -> assertThat(user.getName()).isEqualTo("name"),
                () -> assertThat(user.getEmail()).isEqualTo("user1@slipp.net"),
                () -> assertThat(user.getUserId()).isEqualTo("user1"),
                () -> assertThat(user.getPassword()).isEqualTo("password"),
                () -> assertThat(user.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(user.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void 아이디로_사용자를_조회한다() {
        Optional<User> expected = userRepository.findByUserId(user.getUserId());
        assertAll(
            () -> assertThat(expected.isPresent()).isTrue(),
            () -> assertThat(expected.get()).isEqualTo(user)
        );
    }

    public static User user() {
        return new User("user1", "password", "name", "user1@slipp.net");
    }
}