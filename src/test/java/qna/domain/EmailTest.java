package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void generate_email() {
        // given
        Email actual = Email.of("gmail");
        // when
        Email expect = Email.of("gmail");
        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void email_empty_exception() {
        // when // then
        assertThatThrownBy(() -> Email.of("")).isInstanceOf(IllegalArgumentException.class);
    }

}