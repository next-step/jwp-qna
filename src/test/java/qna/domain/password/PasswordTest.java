package qna.domain.password;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals() {
        Password actual = Password.of("1");
        Password expected = Password.of("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void notEquals() {
        Password actual = Password.of("1");
        Password expected = Password.of("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

}
