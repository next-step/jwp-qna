package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_저장() {
        User actual = new User("userId", "password", "name", "email");
        User expected = userRepository.save(actual);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void id로_유저_조회() {
        User actual = new User("userId", "password", "name", "email");
        userRepository.save(actual);
        Optional<User> expected = userRepository.findByUserId("userId");
        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(actual);
    }
}
