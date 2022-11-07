package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoryTest {
    
    @DisplayName("삭제이력 동등성 비교 확인")
    @Test
    void equals() {
        DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI);
        DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI);

        assertThat(deleteHistory1).isEqualTo(deleteHistory2);
    }
}