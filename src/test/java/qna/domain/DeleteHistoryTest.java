package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:DeleteHistory")
class DeleteHistoryTest {

    @Test
    @DisplayName("삭제 이력 생성")
    public void createDeleteHistoryTest() {
        // Given
        final ContentType contentType = ContentType.ANSWER;
        final Long contentId = 1L;
        final Long deletedById = 1L;
        final LocalDateTime createDate = LocalDateTime.now();

        // When
        DeleteHistory given = new DeleteHistory(contentType, contentId, deletedById, createDate);

        // Then
        assertThat(given).isEqualTo(new DeleteHistory(contentType, contentId, deletedById, createDate));
    }
}
