package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    @DisplayName("비밀번호가 null 이면 예외를 던진다.")
    void fromException1() {
        Assertions.assertThatThrownBy(() -> Password.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비밀번호가 20자 초과이면 예외를 던진다.")
    void fromException2() {
        Assertions.assertThatThrownBy(() -> Password.from("123456789012345678901"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals 테스트 (동일한 경우)")
    void equals1() {
        Password pw1 = Password.from("pw1");
        Password pw2 = Password.from("pw1");
        Assertions.assertThat(pw1).isEqualTo(pw2);
    }

    @Test
    @DisplayName("equals 테스트 (동일하지 않은 경우)")
    void equals2() {
        Password pw1 = Password.from("pw1");
        Password pw2 = Password.from("pw2");
        Assertions.assertThat(pw1).isNotEqualTo(pw2);
    }
}