package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.persistence.Embeddable;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Embeddable
public class UserIdTest {

//    @Column(length = 20, nullable = false, unique = true)

    @ParameterizedTest
    @ValueSource(strings = {"wdq", "wq2", "1313"})
    void new_성공(String source) {
        assertDoesNotThrow(() -> new UserId(source));
    }

    @DisplayName("new_예외_null")
    @Test
    void new_예외_null() {
        // Given
        String source = null;

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new UserId(source));
    }

    @DisplayName("new_예외_empty")
    @Test
    void new_예외_empty  () {
        // Given
        String source = "";

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new UserId(source));
    }

    @DisplayName("new_예외_길이_제한_초과")
    @Test
    void new_예외_길이_제한_초과() {
        // Given
        StringBuilder sourceBuilder = new StringBuilder();
        for (int i = 0; i < UserId.MAXIMUM_LENGTH; i++)
            sourceBuilder.append("  ");

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new UserId(sourceBuilder.toString()));
    }

    @DisplayName("equals_성공")
    @ParameterizedTest
    @ValueSource(strings = {"id1", "id2", "id3"})
    void equals_성공(String source) {
        // Given
        UserId userId1 = new UserId(source);
        UserId userId2 = new UserId(source);

        // When, Then
        assertEquals(userId1, userId2);
    }
}
