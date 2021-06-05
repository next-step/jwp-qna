package qna.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringValidateUtilsTest {

    @DisplayName("Null, Empty, 길이가 긴 문자열의 유효성 체크 테스트")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "123456")
    void validate(final String value) {
        // given
        final int length = 5;

        // when
        final Throwable throwable = catchThrowable(() -> StringValidateUtils.validate(value, length));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
