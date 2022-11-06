package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
}
