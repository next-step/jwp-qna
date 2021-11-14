package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsernameTest {
    @DisplayName("사용자 이름은 1자 이상 20자 이하로 입력해야 한다")
    @Test
    void create() {
        assertThatThrownBy(() -> new Username(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_USERNAME_MESSAGE.getMessage());

        assertThatThrownBy(() -> new Username(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_USERNAME_MESSAGE.getMessage());

        assertThatThrownBy(() -> new Username("1234567891011121314151617"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_USERNAME_MESSAGE.getMessage());

        assertDoesNotThrow(() -> new Username("test"));
    }

}
