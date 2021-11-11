package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DeleteHistoryTest {

    @DisplayName("DeleteHistory를 생성한다")
    @Test
    void testCrete() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        assertAll(
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(deleteHistory.getContentType()),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(deleteHistory.getContentId()),
            () -> assertThat(deleteHistory.getDeletedById()).isEqualTo(deleteHistory.getDeletedById()),
            () -> assertThat(deleteHistory.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS))
        );
    }
}
