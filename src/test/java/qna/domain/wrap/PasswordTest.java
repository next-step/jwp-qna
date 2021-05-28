package qna.domain.wrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PasswordTest {
    @Test
    @DisplayName("비밀번호가 null이면 IllegalArgumentException이 발생한다")
    void 비밀번호가_null이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Password(null));
    }

    @Test
    @DisplayName("비밀번호가 20글자 초과시 IllegalArguementException이 발생한다")
    void 비밀번호가_20글자_초과시_IllegalArgumentException이_발생한다() {
        String password = IntStream.range(0, 21)
                .mapToObj((i) -> "0")
                .reduce("", (before, str) -> before + str);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Password(password));
    }

    @Test
    @DisplayName("비밀번호가 null도 아니고, 20글자 이하면은 정상이다")
    void 비밀번호가_null도_아니고_20글자_이하면은_정상이다() {
        assertDoesNotThrow(() -> new Password("asdf"));
    }
}