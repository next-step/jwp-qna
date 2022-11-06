package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    @DisplayName("이메일 길이가 50자가 넘는 경우 예외를 발생시킨다.")
    void from() {
        Assertions.assertThatThrownBy(() -> Email.from("123456789012345678901234567890123456789012345678901"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        Email email1 = Email.from("test@email.com");
        Email email2 = Email.from("test@email.com");
        Assertions.assertThat(email1).isEqualTo(email2);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        Email email1 = Email.from("test@email.com");
        Email email2 = Email.from("test123@email.com");
        Assertions.assertThat(email1).isNotEqualTo(email2);
    }
}