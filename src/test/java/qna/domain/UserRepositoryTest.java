package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_생성() {

        User expected = new User(1L, "soob", "password", "name", "soob@slipp.net");
        User actual = userRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void 아이디_유저_조회() {
        User expected = userRepository.save(new User(1L, "soob", "password", "name", "soob@slipp.net"));
        Optional<User> actual = userRepository.findByUserId(expected.getUserId());

        assertAll(
                () -> assertThat(actual.get().getId()).isNotNull(),
                () -> assertThat(actual.get().getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.get().getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.get().getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.get().getEmail()).isEqualTo(expected.getEmail())
        );
    }
}
