package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    
    @Autowired
    UserRepository users;

    @Test
    void save() {
        User expected = UserTest.JAVAJIGI;
        User actual = users.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findById() {
        User expected = users.save(UserTest.SANJIGI);
        Optional<User> actual = users.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
        assertThat(actual.get() == expected).isTrue();
    }

    @Test
    void update() {
        String name = "mwkwon";
        User expected = users.save(UserTest.JAVAJIGI);
        expected.setName(name);
        Optional<User> actual = users.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    @Test
    void delete() {
        User expected = users.save(UserTest.SANJIGI);
        users.delete(expected);
        Optional<User> actual = users.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
