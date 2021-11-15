package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;

    @Test
    void save() {
        final User actual = users.save(JAVAJIGI);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void findByUserId() {
        final User user1 = users.save(JAVAJIGI);
        final User user2 = users.findByUserId(JAVAJIGI.getUserId()).get();

        assertAll(
            () -> assertThat(user2.getId()).isNotNull(),
            () -> assertThat(user2.getUserId()).isEqualTo(user1.getUserId()),
            () -> assertThat(user2.getPassword()).isEqualTo(user1.getPassword()),
            () -> assertThat(user2.getName()).isEqualTo(user1.getName()),
            () -> assertThat(user2.getEmail()).isEqualTo(user1.getEmail())
        );
    }

    @Test
    void findByUserIdAndPassword() {
        final User user1 = users.save(JAVAJIGI);
        final User user2 = users.findByUserIdAndPassword(JAVAJIGI.getUserId(), JAVAJIGI.getPassword());
        assertAll(
            () -> assertThat(user2.getId()).isNotNull(),
            () -> assertThat(user2.getUserId()).isEqualTo(user1.getUserId()),
            () -> assertThat(user2.getPassword()).isEqualTo(user1.getPassword()),
            () -> assertThat(user2.getName()).isEqualTo(user1.getName()),
            () -> assertThat(user2.getEmail()).isEqualTo(user1.getEmail())
        );
    }
}
