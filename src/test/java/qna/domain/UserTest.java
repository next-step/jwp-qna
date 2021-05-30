package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI =
            new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI =
            new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;

    @Test
    void save() {
        User actual = users.save(JAVAJIGI);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName())
        );
    }

    @Test
    void findByUserId() {
        String expected = JAVAJIGI.getUserId();
        users.save(JAVAJIGI);
        String actual = users.findByUserId(expected).get().getUserId();
    }
}
