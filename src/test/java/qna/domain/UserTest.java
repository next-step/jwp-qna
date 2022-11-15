package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    @Test
    void userId_is_not_null_test() {
        assertThatThrownBy(() -> User.create(""))
                .isInstanceOf(ForbiddenException.class);
    }
    @Test
    void password_is_not_null_test() {
        assertThatThrownBy(() -> User.create("reina", "", "name"))
                .isInstanceOf(ForbiddenException.class);
    }
    @Test
    void name_is_not_null_test() {
        assertThatThrownBy(() -> User.create("reina", "password", ""))
                .isInstanceOf(ForbiddenException.class);
    }
}
