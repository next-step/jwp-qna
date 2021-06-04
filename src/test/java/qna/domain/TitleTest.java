package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class TitleTest {

    @DisplayName("Null 이나 비어있는 문자열을 생성자에 argument 로 전달했을 때 예외가 발생하는지 테스트")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"      "})
    void given_NullAndEmptyText_when_ConstructTitle_then_ThrownIllegalArgumentException(String input) {
        // when
        Throwable throwable = catchThrowable(() -> new Title(input));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("긴 문자열을 생성자에 argument 로 전달했을 때 예외가 발생하는지 테스트")
    @Test
    void given_TooLongText_whenConstructTitle_then_ThrownIllegalArgumentException() {
        // given
        String tooLongText = tooLongText();

        // when
        Throwable throwable = catchThrowable(() -> new Title(tooLongText));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    private String tooLongText() {
        return IntStream.rangeClosed(1, 101)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(""));
    }
}
