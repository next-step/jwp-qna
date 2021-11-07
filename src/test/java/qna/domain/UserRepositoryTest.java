package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자를_저장한다() {
        // when
        User expected = userRepository.save(user());

        // then
        assertAll(
                () -> assertThat(expected.getName()).isEqualTo("name"),
                () -> assertThat(expected.getEmail()).isEqualTo("user1@slipp.net"),
                () -> assertThat(expected.getUserId()).isEqualTo("user1"),
                () -> assertThat(expected.getPassword()).isEqualTo("password"),
                () -> assertThat(expected.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(expected.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }

    public static User user() {
        return new User("user1", "password", "name", "user1@slipp.net");
    }
}