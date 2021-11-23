package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {
    private static final String LENGTH_20_TEXT = "12345678901234567890";

    @DisplayName("비밀번호 생성")
    @Test
    void constructPassword_success() {
        Password password = new Password(LENGTH_20_TEXT);
        assertThat(password).isEqualTo(new Password(LENGTH_20_TEXT));
    }

    @DisplayName("잘못된 형식으로 비밀번호 생성 시 에러")
    @Test
    void constructNullPassword_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Password(null))
            .withMessage("길이 20이하의 비어있지 않은 비밀번호를 입력해주세요.");
    }

    @DisplayName("잘못된 형식으로 비밀번호 생성 시 에러")
    @Test
    void constructPasswordWithLength101_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Password(LENGTH_20_TEXT + 1))
            .withMessage("길이 20이하의 비어있지 않은 비밀번호를 입력해주세요.");
    }
}