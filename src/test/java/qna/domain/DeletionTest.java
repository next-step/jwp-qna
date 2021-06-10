package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Deleted Wrapping 테스트")
class DeletionTest {

    @DisplayName("new_성공")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void new_성공(boolean source) {
        assertDoesNotThrow(() -> new Deletion(source));
    }

    @DisplayName("isDeleted_성공")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isDeleted_성공(boolean source) {
        // Given
        Deletion deletion = new Deletion(source);

        // When, Then
        assertThat(deletion.isDeleted()).isEqualTo(source);
    }

    @DisplayName("delete_성공")
    @Test
    void delete_성공() {
        // Given
        Deletion deletion = new Deletion(false);

        // When, Then
        assertDoesNotThrow(() -> deletion.delete());
    }

    @DisplayName("delete_예외_이미_삭제된_건")
    @Test
    void delete_예외_이미_삭제된_건() throws CannotDeleteException {
        // Given
        Deletion deletion = new Deletion(false);

        // When
        deletion.delete();

        // When, Then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> deletion.delete());
    }
}