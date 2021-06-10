package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Name 테스트")
class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"wdq", "wq2", "1313"})
    void new_성공(String source) {
        assertDoesNotThrow(() -> new Name(source));
    }

    @DisplayName("new_예외_null")
    @Test
    void new_예외_null() {
        // Given
        String source = null;

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Name(source));
    }

    @DisplayName("new_예외_길이_제한_초과")
    @Test
    void new_예외_길이_제한_초과() {
        // Given
        StringBuilder sourceBuilder = new StringBuilder();
        for (int i = 0; i < Name.MAXIMUM_LENGTH; i++)
            sourceBuilder.append("  ");

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Name(sourceBuilder.toString()));
    }

    @DisplayName("equals_성공")
    @ParameterizedTest
    @ValueSource(strings = {"id1", "id2", "id3"})
    void equals_성공(String source) {
        // Given
        Name name1 = new Name(source);
        Name name2 = new Name(source);

        // When, Then
        assertEquals(name1, name2);
    }
}
