package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("이메일은 영문과 숫자로된 아이디 + @ + 도메인주소의 형식을 가진다. 예를들어 test1234@neststep.camp")
    @Test
    void validateEmailType() {
        //given
        String email = "test1234@nextstep.camp";

        //when
        Email actual = new Email(email);

        //then
        assertThat(actual.getEmail()).isEqualTo(email);
    }

    @DisplayName("영문과 숫자로된 아이디 + @ + 도메인주소의 형식을 갖지 않는다면 예외를 발생시킨다.")
    @Test
    void validateEmailTypeException() {
        //when
        assertThatThrownBy(() -> new Email("test"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Email.INVALID_EMAIL_MESSAGE);
    }

    @DisplayName("이메일 길이가 50자리 이상일 때 예외를 발생시킨다.")
    @Test
    void validateEmailLengthException() {
        //when
        assertThatThrownBy(() -> new Email("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest@test.com"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Email.INVALID_EMAIL_LENGTH_MESSAGE);
    }
}