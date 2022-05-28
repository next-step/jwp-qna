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
        User expected = userRepository.save(new User(1L, "soob", "password", "name", "soob@slipp.net"));
        User actual = userRepository.save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 아이디_유저_조회() {
        User expected = userRepository.save(new User(1L, "soob", "password", "name", "soob@slipp.net"));
        Optional<User> actual = userRepository.findByUserId("soob");
        assertThat(actual.get()).isEqualTo(expected);
    }
}
