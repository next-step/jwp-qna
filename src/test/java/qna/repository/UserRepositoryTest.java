package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("mins99", "1234", "ms", "mins99@slipp.net");
    }

    @Test
    void save() {
        // given
        User expected = new User("woowahan", "tech", "woowahan", "woowahan@slipp.net");

        // when
        User actual = userRepository.save(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    @Test
    void update() {
        // given
        User expected = userRepository.save(user);
        String name = expected.getName();

        // when
        expected.setName(name + "2");
        User actual = userRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual.getName()).isNotEqualTo(name);
    }

    @Test
    void delete() {
        // given
        User expected = userRepository.save(user);

        // when
        userRepository.delete(expected);
        Optional<User> actual = userRepository.findById(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findById() {
        // given
        User expected = userRepository.save(user);

        // when
        User actual = userRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByUserId() {
        // given
        User expected = userRepository.save(user);

        // when
        User actual = userRepository.findByUserId(expected.getUserId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
