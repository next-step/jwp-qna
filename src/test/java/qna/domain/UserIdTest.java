package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserIdTest {
    @DisplayName("사용자 아이디는 1자 이상 20자 이하로 입력해야 한다")
    @Test
    void create() {
        assertThatThrownBy(() -> new UserId(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_USER_ID_MESSAGE.getMessage());

        assertThatThrownBy(() -> new UserId(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_USER_ID_MESSAGE.getMessage());

        assertThatThrownBy(() -> new UserId("1234567891011121314151617"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_USER_ID_MESSAGE.getMessage());

        assertDoesNotThrow(() -> new UserId("test"));
    }

}
