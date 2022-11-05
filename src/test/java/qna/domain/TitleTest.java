package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTest {

    @Test
    @DisplayName("제목이 null 이면 예외를 던진다.")
    void fromException1() {
        Assertions.assertThatThrownBy(() -> Title.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("제목이 100자 초과이면 예외를 던진다.")
    void fromException2() {
        Assertions.assertThatThrownBy(() -> Title.from(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        Title actual = Title.from("1");
        Title expected = Title.from("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        Title actual = Title.from("1");
        Title expected = Title.from("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }
}