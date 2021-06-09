package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @DisplayName("비밀번호는 영문(대소문자 구분) + 숫자 + 특수문자 조합으로 이루어져있다.")
    @Test
    void validatePasswordType() {
        //given
        String password = "tESt@1234";

        //when
        Password actual = new Password(password);

        //then
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @DisplayName("비밀번호에 소문자,대문자,숫자,특수문자 중 하나라도 없다면 예외를 발생시킨다.")
    @Test
    void validatePasswordTypeException() {
        //when
        assertThatThrownBy(() -> new Password("testtest12"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_MESSAGE);
        assertThatThrownBy(() -> new Password("test1332AAAA"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_MESSAGE);
        assertThatThrownBy(() -> new Password("test@QQQQQQQQ"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_MESSAGE);
        assertThatThrownBy(() -> new Password("TEST12345123"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_MESSAGE);
    }

    @DisplayName("비밀번호 길이가 20자리 초과할 때 예외를 발생시킨다.")
    @Test
    void validateExceededPasswordLengthException() {
        //when
        assertThatThrownBy(() -> new Password("testtesttesttesttesttesttesttesttesttesttesttesttest"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_LENGTH_MESSAGE);
    }

    @DisplayName("비밀번호 길이가 9자리 미만일 때 예외를 발생시킨다.")
    @Test
    void validateLittlePasswordLengthException() {
        //when
        assertThatThrownBy(() -> new Password("Test12@"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Password.INVALID_PASSWORD_LENGTH_MESSAGE);
    }
}