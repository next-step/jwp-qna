package qna.domain.wrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailTest {
    @Test
    @DisplayName("이메일이 null이면 IllegalArgumentException이 발생한다")
    void 이메일이_null이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Email(null));
    }

    @Test
    @DisplayName("이메일 형식이 아니면 IllegalArgumentException이 발생한다")
    void 이메일_형식이_아니면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Email("asdfasdfasdf"));
    }

    @Test
    @DisplayName("이메일이 50글자 초과시 IllegalArguementException이 발생한다")
    void 이메일이_50글자_초과시_IllegalArgumentException이_발생한다() {
        String email = IntStream.range(0, 51)
                .mapToObj((i) -> "0")
                .reduce("", (before, str) -> before + str)
                + "@naver.com";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Email(email));
    }

    @Test
    @DisplayName("이메일이 null도 아니고, 50글자 이하면서 이메일 정규식에 맞으면 정상이다")
    void 이메일이_null도_아니고_50글자_이하면서_이메일_정규식에_맞으면_정상이다() {
        assertDoesNotThrow(() -> new Email("soora_genius@naver.com"));
    }
}