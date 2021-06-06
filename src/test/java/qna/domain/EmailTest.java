package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("기본적인 이메일 형식을 가져야 하며 최대 50자리가 넘지 않아야 한다.")
    @Test
    void validateEmailType() {
        //given
        String email = "test@nextstep.camp";

        //when
        Email actual = new Email(email);

        //then
        assertThat(actual.getEmail()).isEqualTo(email);
    }

    @Test
    void validateEmailTypeException() {
        //when
        assertThatThrownBy(() -> new Email("test"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Email.INVALID_EMAIL_MESSAGE);
    }

    @Test
    void validateEmailLengthException() {
        //when
        assertThatThrownBy(() -> new Email("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest@test.com"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Email.INVALID_EMAIL_MESSAGE);
    }
}