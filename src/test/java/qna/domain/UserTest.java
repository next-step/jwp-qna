package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    @Test
    void userId_is_not_null_test() {
        assertThatThrownBy(() -> TestUserFactory.create("reina"))
                .isInstanceOf(ForbiddenException.class);
    }
    @Test
    void password_is_not_null_test() {
        assertThatThrownBy(() -> TestUserFactory.create("reina"))
                .isInstanceOf(ForbiddenException.class);
    }
    @Test
    void name_is_not_null_test() {
        assertThatThrownBy(() -> TestUserFactory.create("reina"))
                .isInstanceOf(ForbiddenException.class);
    }
}
