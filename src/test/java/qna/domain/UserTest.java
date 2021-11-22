package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UserTest {

    public static final User JAVAJIGI =
        new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI =
        new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void equals() {
        // given
        final User user = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final User updatedUser = TestUserFactory.create(
            1L, "sanjigi", "password2", "name2", "sanjigi@slipp.net"
        );

        // when, then
        assertThat(user).isEqualTo(updatedUser);
    }
}
