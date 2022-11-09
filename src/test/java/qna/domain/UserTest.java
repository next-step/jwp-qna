package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void userId_is_not_null_test() {
        assertThatThrownBy(() -> new User(null, "password", "name", "email"))
                .isInstanceOf(ForbiddenException.class);
    }
    @Test
    void password_is_not_null_test() {
        assertThatThrownBy(() -> new User("userId", null, "name", "email"))
                .isInstanceOf(ForbiddenException.class);
    }
    @Test
    void name_is_not_null_test() {
        assertThatThrownBy(() -> new User("userId", "password", null, "email"))
                .isInstanceOf(ForbiddenException.class);
    }
}
