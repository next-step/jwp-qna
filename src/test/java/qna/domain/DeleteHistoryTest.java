package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    @Test
    @DisplayName("answer 정상 생성 테스트")
    void create() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

        assertThat(history)
                .isEqualTo(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
    }
}
