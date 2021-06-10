package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    @Autowired
    UserRepository users;

    @Test
    void save() {
        // given
        User expected = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

        // when
        User actual = users.save(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByUserId() {
        // given
        User user = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        users.save(user);

        // when
        Optional<User> actual = users.findByUserId(user.userId());

        // then

        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get().userId()).isEqualTo(user.userId()),
                () -> assertThat(actual.get()).isEqualTo(user)
        );

    }
}
