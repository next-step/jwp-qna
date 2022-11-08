package qna.domain.title;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals() {
        Title actual = Title.of("1");
        Title expected = Title.of("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void notEquals() {
        Title actual = Title.of("1");
        Title expected = Title.of("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

}
