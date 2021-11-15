package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {
    @DisplayName("이메일은 50자 이내로 입력해야 한다")
    @Test
    void create() {
        StringBuilder longText = new StringBuilder();
        IntStream.range(0, 50)
            .forEach(longText::append);

        assertThatThrownBy(() -> new Email(longText.toString()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(VALIDATE_EMAIL_MESSAGE.getMessage());

        assertDoesNotThrow(() -> new Email("test"));
        assertDoesNotThrow(() -> new Email(null));
        assertDoesNotThrow(() -> new Email(""));
    }
}
