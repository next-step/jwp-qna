package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class UserIdTest {

    @DisplayName("Null, Empty, 길이가 긴 텍스트를 생성자의 argument 로 전달하면 예외가 발생하는지 테스트")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456789012345678901", "일이삼사오육칠팔구십일이삼사오육칠팔구십일"})
    void given_InvalidIdText_when_Construct_then_ThrownIllegalArgumentException(final String id) {
        // when
        final Throwable throwable = catchThrowable(() -> new UserId(id));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
