package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

class PasswordTest {

    @Test
    void 비밀번호_생성() {
        //given
        Password actual = Password.of("password");

        //when
        Password expect = Password.of("password");

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 비밀번호_다름_테스트() {
        //given
        Password actual = Password.of("password");

        //when
        Password expect = Password.of("wrongpassword");

        //then
        assertThat(actual).isNotEqualTo(expect);
    }

    @Test
    void 비밀번호_toString() {
        //given
        String actual = "password";
        Password password = Password.of(actual);

        //then
        assertThat(password.toString()).contains("Password{password='" + actual);
    }

    @Test
    void 서로_다른_유저_Id일_경우_예외를_발생시킨다() {
        //given
        Password actual = Password.of("password");
        Password expect = Password.of("wrongpassword");

        //when
        assertThatThrownBy(() -> actual.validateMatchPassword(expect)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 비밀번호_길이가_길면_예외를_발생시킨다() {
        //given
        String actual = "12345678901234567890123";

        //then
        assertThatThrownBy(() -> Password.of(actual)).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorCode.비밀번호의_길이가_너무_김.getErrorMessage());
    }
}
