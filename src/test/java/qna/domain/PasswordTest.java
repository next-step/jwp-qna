package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    @DisplayName("비밀번호가 공백일 경우 exception")
    void 비밀번호_공백_예외() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> Password.from(""))
            .withMessage("비밀번호는 널 또는 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호가 NULL일 경우 exception")
    void 비밀번호_NULL_예외() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> Password.from(null))
            .withMessage("비밀번호는 널 또는 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호 생성")
    void 비밀번호_생성() {
        Password result = Password.from("test");

        assertThat(result).isEqualTo(Password.from("test"));
    }

}