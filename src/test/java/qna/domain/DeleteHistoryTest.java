package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DeleteHistoryTest {

    @DisplayName("DeleteHistory를 생성한다")
    @ParameterizedTest
    @CsvSource(value = {"ANSWER:1:2"}, delimiter = ':')
    void testCrete(ContentType contentType, Long contentId, Long deletedById) {
        DeleteHistory deleteHistory = new DeleteHistory(contentType, contentId, deletedById, LocalDateTime.now());
        assertAll(
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(contentType),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(contentId),
            () -> assertThat(deleteHistory.getDeletedById()).isEqualTo(deletedById),
            () -> assertThat(deleteHistory.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS))
        );
    }
}
