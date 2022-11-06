package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import qna.constant.ErrorCode;

class EmailTest {

    @Test
    void 이메일_생성() {
        //given
        Email actual = Email.of("javajigi@gmail.com");

        //when
        Email expect = Email.of("javajigi@gmail.com");

        //then
        assertAll(
                () -> assertThat(actual).isEqualTo(expect),
                () -> assertThat(actual.isEqualEmail(expect)).isTrue()
        );
    }

    @Test
    void 이메일_다름_테스트() {
        //given
        Email actual = Email.of("javajigi@gmail.com");

        //when
        Email expect = Email.of("sanjigi@gmail.com");

        //then
        assertAll(
                () -> assertThat(actual).isNotEqualTo(expect),
                () -> assertThat(actual.isEqualEmail(expect)).isFalse()
        );
    }

    @Test
    void 이메일_toString() {
        //given
        String actual = "javajigi@gmail.com";
        Email email = Email.of(actual);

        //then
        assertThat(email.toString()).contains("Email{email='" + actual);
    }

    @Test
    void 이메일_길이가_길면_예외를_발생시킨다() {
        //given
        String actual = "12345678901234567890123456789012345678901234567890123";

        //then
        assertThatThrownBy(() -> Email.of(actual)).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorCode.이메일의_길이가_너무_김.getErrorMessage());
    }
}
