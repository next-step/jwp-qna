package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
}
