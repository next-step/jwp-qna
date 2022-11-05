package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentsTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        Contents actual = Contents.from("1");
        Contents expected = Contents.from("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        Contents actual = Contents.from("1");
        Contents expected = Contents.from("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }
}