package qna.domain.wrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NameTest {
    @Test
    @DisplayName("이름가 null이면 IllegalArgumentException이 발생한다")
    void 이름가_null이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Name(null));
    }

    @Test
    @DisplayName("이름가 20글자 초과시 IllegalArguementException이 발생한다")
    void 이름가_20글자_초과시_IllegalArgumentException이_발생한다() {
        String userId = IntStream.range(0, 21)
                .mapToObj((i) -> "0")
                .reduce("", (before, str) -> before + str);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Name(userId));
    }

    @Test
    @DisplayName("이름가 null도 아니고, 20글자 이하면은 정상이다")
    void 이름가_null도_아니고_20글자_이하면은_정상이다() {
        assertDoesNotThrow(() -> new Name("asdf"));
    }
}