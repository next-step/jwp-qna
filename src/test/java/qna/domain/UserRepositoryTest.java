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
        User expected = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getName()).isEqualTo("name"),
                () -> assertThat(expected.getEmail()).isEqualTo("javajigi@slipp.net"),
                () -> assertThat(expected.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(expected.getPassword()).isEqualTo("password"),
                () -> assertThat(expected.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(expected.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }
}