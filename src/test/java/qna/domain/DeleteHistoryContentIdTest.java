package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoryContentIdTest {
    @DisplayName("save deleteHistory contentId")
    @Test
    void getDeleteHistoryContentId() {
        DeleteHistoryContentId deleteHistoryContentId = new DeleteHistoryContentId(2L);

        assertThat(deleteHistoryContentId.getContentId()).isEqualTo(2L);
    }
}