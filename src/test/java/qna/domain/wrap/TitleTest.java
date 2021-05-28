package qna.domain.wrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TitleTest {
    @Test
    @DisplayName("제목이 null이면 IllegalArgumentException이 발생한다")
    void 제목이_null이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Title(null));
    }

    @Test
    @DisplayName("제목은 100글자 초과시 IllegalArguementException이 발생한다")
    void 제목은_100글자_초과시_IllegalArgumentException이_발생한다() {
        String title = IntStream.range(0, 101)
                .mapToObj((i) -> "0")
                .reduce("", (before, str) -> before + str);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Title(title));
    }

    @Test
    @DisplayName("제목이 null도 아니고, 100글자 이하면은 정상이다")
    void 제목이_null도_아니고_100글자_이하면은_정상이다() {
        assertDoesNotThrow(() -> new Title("asdf"));
    }
}