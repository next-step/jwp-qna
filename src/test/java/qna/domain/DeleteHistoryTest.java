package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DeleteHistoryTest {

    @DisplayName("DeleteHistory를 생성한다")
    @ParameterizedTest
    @CsvSource(value = {"ANSWER:1"}, delimiter = ':')
    void testCrete(ContentType contentType, Long contentId) {
        User deletedBy = new User("userId", "password", "name", "email");
        DeleteHistory deleteHistory = DeleteHistory.of(contentType, contentId, deletedBy, LocalDateTime.now());
        assertAll(
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(contentId),
            () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(deletedBy),
            () -> assertThat(deleteHistory.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS))
        );
    }

    @DisplayName("삭제자를 입력하지 않으면 오류를 던진다")
    @Test
    void givenNullDeletedByThenThrowException() {
        assertThatThrownBy(() -> DeleteHistory.of(ContentType.ANSWER, 1L, null, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
