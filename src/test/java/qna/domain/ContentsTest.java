package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Contents Wrapping 클래스 테스트")
class ContentsTest {

    @DisplayName("new_성공")
    @ParameterizedTest
    @ValueSource(strings = {"", " 1", "2234"})
    void new_성공(String source) {
        assertDoesNotThrow(() -> new Contents(source));
    }

    @DisplayName("new_예외_null")
    @Test
    void new_예외_null() {
        // Given
        String source = null;

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Contents(source));
    }

    @DisplayName("new_예외_길이_제한_초과")
    @Test
    void new_예외_길이_제한_초과() {
        // Given
        StringBuilder sourceBuilder = new StringBuilder();
        for (int i = 0; i < Contents.MAXIMUM_LENGTH; i++)
            sourceBuilder.append("  ");

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Contents(sourceBuilder.toString()));
    }

    @DisplayName("equals_성공")
    @ParameterizedTest
    @ValueSource(strings = {"", " 1", "2234"})
    void equals_성공(String source) {
        // Given
        Contents contents1 = new Contents(source);
        Contents contents2 = new Contents(source);

        // When, Then
        assertEquals(contents1, contents2);
    }


}