package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {
    @DisplayName("비밀번호는 1자 이상 20자 이하로 입력해야 한다")
    @Test
    void create() {
        assertThatThrownBy(() -> new Password(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_PASSWORD_MESSAGE.getMessage());

        assertThatThrownBy(() -> new Password(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_PASSWORD_MESSAGE.getMessage());

        assertThatThrownBy(() -> new Password("1234567891011121314151617"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_PASSWORD_MESSAGE.getMessage());

        assertDoesNotThrow(() -> new Password("test"));
    }
}
