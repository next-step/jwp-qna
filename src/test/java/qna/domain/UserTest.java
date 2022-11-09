package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        assertAll(
                () -> assertDoesNotThrow(() -> userRepository.save(JAVAJIGI)),
                () -> assertDoesNotThrow(() -> userRepository.save(SANJIGI))
        );
    }

    @Test
    void findByUserId() {
        final User user = new User("ndka134yg", "1234", "사용자 1", "user-1@email.com");
        final User savedUser = userRepository.save(user);

        final User foundUser = userRepository.findByUserId(savedUser.getUserId()).get();
        assertAll(
                () -> assertThat(foundUser.getId()).isPositive(),
                () -> assertThat(foundUser.getUserId()).isEqualTo(savedUser.getUserId()),
                () -> assertThat(foundUser.getName()).isEqualTo(savedUser.getName()),
                () -> assertThat(foundUser.getEmail()).isEqualTo(savedUser.getEmail())
        );
    }
}
