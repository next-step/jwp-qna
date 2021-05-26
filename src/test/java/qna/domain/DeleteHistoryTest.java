package qna.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DeleteHistoryTest {

    @Test
    void canCreate() {
        new DeleteHistory(
                ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    }
}
