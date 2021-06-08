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

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        users.save(user1);
        users.save(user2);
    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(user1.getId()).isNotNull(),
                () -> assertThat(user1.getName()).isEqualTo(user1.getName())
        );
    }

    @Test
    void findByUserId() {
        String expected = user2.getUserId();

        Optional<User> actual = users.findByUserId(expected);
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get().getUserId()).isEqualTo(user2.getUserId())
        );

    }
}
