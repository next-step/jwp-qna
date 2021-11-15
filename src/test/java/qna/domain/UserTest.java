package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {

    @Autowired
    private UserRepository users;

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void saveUser() {
        User savedUser = users.save(JAVAJIGI);

        assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void findByUserId() {
        User savedUser = users.save(JAVAJIGI);
        Optional<User> foundUser = users.findByUserId(JAVAJIGI.getUserId());

        assertAll(
            () -> assertThat(foundUser).isPresent(),
            () -> assertThat(foundUser.get().getId()).isNotNull(),
            () -> assertThat(foundUser.get().getName()).isEqualTo(savedUser.getName())
        );
    }

    @Test
    void updateUser() {
        User savedUser = users.save(JAVAJIGI);
        User foundUser = users.findByUserId(JAVAJIGI.getUserId()).get();
        foundUser.setUserId(SANJIGI.getUserId());

        Optional<User> foundUserByUserId = users.findByUserId(SANJIGI.getUserId());

        assertAll(
            () -> assertThat(foundUserByUserId).isPresent(),
            () -> assertThat(foundUser).isSameAs(foundUserByUserId.get())
        );
    }

    @Test
    void deleteUser() {
        User savedUser = users.save(JAVAJIGI);
        User foundUser = users.findByUserId(JAVAJIGI.getUserId()).get();

        users.delete(foundUser);

        Optional<User> foundUserByUserId = users.findByUserId(foundUser.getUserId());

        assertAll(
            () -> assertThat(foundUserByUserId).isNotPresent()
        );
    }
}
