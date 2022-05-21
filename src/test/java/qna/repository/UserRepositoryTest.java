package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        // given & when
        User user = userRepository.save(UserTest.JAVAJIGI);

        // then
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword())
        );
    }

    @Test
    void update() {
        // given
        User user = userRepository.save(UserTest.JAVAJIGI);
        String expected = user.getName();

        // when
        user.setName("javajigi2");
        User actual = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isNotEqualTo(expected);

    }

    @Test
    void delete() {
        // given
        User expected = userRepository.save(UserTest.JAVAJIGI);

        // when
        userRepository.delete(expected);
        Optional<User> actual = userRepository.findById(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findById() {
        // given
        User expected = userRepository.save(UserTest.JAVAJIGI);

        // when
        User actual = userRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByUserId() {
        // given
        User expected = userRepository.save(UserTest.JAVAJIGI);

        // when
        User actual = userRepository.findByUserId(expected.getUserId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
