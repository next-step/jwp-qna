package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTestFixture.JAVAJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoryTest {
    @Test
    @DisplayName("답변 삭제 이력 생성")
    void create_answer_delete_history() {
        DeleteHistory actual = DeleteHistory.answerOf(1L, JAVAJIGI);
        assertThat(actual).isEqualTo(new DeleteHistory(ContentType.ANSWER, 1L, JAVAJIGI));
    }
}
