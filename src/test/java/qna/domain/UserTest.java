package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    @Test
    void password_is_not_null_test() {
        assertThatThrownBy(() -> User.create("reina", "", "name", "reina@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void name_is_not_null_test() {
        assertThatThrownBy(() -> User.create("reina", "password", "","reina@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
