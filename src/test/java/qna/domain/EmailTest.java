package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {
    private static final String LENGTH_50_TEXT = "12345678901234567890123456789012345678901234567890";

    @DisplayName("이메일 생성")
    @Test
    void constructEmail_success() {
        Email email = new Email(LENGTH_50_TEXT);
        assertThat(email).isEqualTo(new Email(LENGTH_50_TEXT));
    }

    @DisplayName("잘못된 형식으로 이메일 생성 시 에러")
    @Test
    void constructEmailWithLength101_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Email(LENGTH_50_TEXT + 1))
            .withMessage("길이 50이하의 이메일 입력해주세요.");
    }
}