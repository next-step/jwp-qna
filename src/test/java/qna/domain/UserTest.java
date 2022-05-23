package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", new UserLogin("id", "password", "javajigi@slipp.net"));
    public static final User SANJIGI = new User(2L, "sanjigi", new UserLogin("id", "password", "sanjigi@slipp.net"));
    public static final User TESTUSER = new User(3L, "testuser", new UserLogin("id", "password", "testuser@slipp.net"));

    @Test
    void update() {
        final User loginUser = new User(3L, "testuser", new UserLogin("id", "password", "testuser@slipp.net"));

        TESTUSER.update(loginUser, SANJIGI);
        assertThat(TESTUSER.getName()).isEqualTo(SANJIGI.getName());
    }

    @Test
    void equalsNameAndEmail() {
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isFalse();
    }

}
