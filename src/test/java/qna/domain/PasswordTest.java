package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @DisplayName("비밀번호는 영문(대소문자 구분) + 숫자 + 특수문자 조합으로 9~20 자리여야한다.")
    @Test
    void validatePasswordType() {
        //given
        String password = "tESt@1234";

        //when
        Password actual = new Password(password);

        //then
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    void validatePasswordTypeException() {
        //when
        assertThatThrownBy(() -> new Password("test"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_MESSAGE);
    }

    @Test
    void validatePasswordLengthException() {
        //when
        assertThatThrownBy(() -> new Password("testtesttesttesttesttesttesttesttesttesttesttesttest"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_MESSAGE);
    }
}