package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given & when & then
        assertAll(
            () -> assertThat(D1).isNotNull(),
            () -> assertThat(D1.getContentId()).isEqualTo(1L),
            () -> assertThat(D1.getContentType()).isEqualTo(ContentType.ANSWER),
            () -> assertThat(D1.getDeletedById()).isEqualTo(1L)
        );
    }
}
