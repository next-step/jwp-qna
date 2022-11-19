package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    @Test
    void generate_password() {
        // given
        Password actual = Password.of("sonny1!");
        // when
        Password expect = Password.of("sonny1!");
        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void password_empty_exception() {
        // when // then
        assertThatThrownBy(() -> Password.of("")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void password_match_test() {
        // given
        Password actual = Password.of("sonny1!");
        // when
        Password expect = Password.of("sonny1!");
        // then
        assertThat(actual.matchPassword(expect)).isTrue();
    }
}