package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import qna.domain.exception.IllegalUserIdException;
import qna.domain.exception.UserIdLengthExceedException;

class UserIdTest {

    @Test
    void test_id가_null_이나_빈_문자열_예외() {
        assertAll(
            () -> assertThatThrownBy(() -> UserId.from(null))
                .isInstanceOf(IllegalUserIdException.class),
            () -> assertThatThrownBy(() -> UserId.from(""))
                .isInstanceOf(IllegalUserIdException.class)
        );
    }

    @Test
    void test_id_길이제한_초과시_예외() {
        String longId = "abcdefghijklmnopqrstuvwxyz";

        assertThatThrownBy(() -> UserId.from(longId))
            .isInstanceOf(UserIdLengthExceedException.class);
    }
}