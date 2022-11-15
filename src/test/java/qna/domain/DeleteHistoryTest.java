package qna.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {
    @Test
    @DisplayName("생성 테스트")
    void deleteHistoryTest() {
        assertThatNoException().isThrownBy(() -> new DeleteHistory(ContentType.QUESTION, 1L, QuestionTest.Q1.getWriterId(), LocalDateTime.now()));
    }
}
