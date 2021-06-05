package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailTest {

    @DisplayName("길이가 긴 문자열을 생성자에 전달했을 때 예외가 발생하는지 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"123456789012345678901234567890123456789012345678901"})
    void given_InvalidIdText_when_Construct_then_ThrownIllegalArgumentException(final String input) {
        // when
        final Throwable throwable = catchThrowable(() -> new Email(input));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
