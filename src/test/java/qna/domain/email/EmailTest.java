package qna.domain.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.content.Contents;

class EmailTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals() {
        Email actual = Email.of("1");
        Email expected = Email.of("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void notEquals() {
        Email actual = Email.of("1");
        Email expected = Email.of("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

}
