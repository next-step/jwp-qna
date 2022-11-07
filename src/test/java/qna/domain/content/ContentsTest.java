package qna.domain.content;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentsTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals() {
        Contents actual = Contents.of("1");
        Contents expected = Contents.of("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void notEquals() {
        Contents actual = Contents.of("1");
        Contents expected = Contents.of("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

}
