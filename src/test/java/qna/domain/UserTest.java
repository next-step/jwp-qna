package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, new UserInfo("javajigi", "id", "password", "javajigi@slipp.net"));
    public static final User SANJIGI = new User(2L, new UserInfo("sanjigi", "id", "password", "sanjigi@slipp.net"));
    public static final User TESTUSER = new User(3L, new UserInfo("testuser", "id", "password", "testuser@slipp.net"));

    @Test
    void update() {
        final User loginUser = new User(3L, new UserInfo("testuser", "id", "password", "testuser@slipp.net"));

        TESTUSER.update(loginUser, SANJIGI);
        assertThat(TESTUSER.getUserInfo().getName()).isEqualTo(SANJIGI.getUserInfo().getName());
    }

    @Test
    void equalsNameAndEmail() {
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isFalse();
    }

}
