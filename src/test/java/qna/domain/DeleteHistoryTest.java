package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.fixture.TestUserFactory;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoryTest {
    @DisplayName("질문에 대한 삭제이력을 만들 수 있다")
    @Test
    void create_question_test() {
        User deleteBy = TestUserFactory.create("서정국");
        DeleteHistory deleteHistory = DeleteHistory.createQuestion(1L, deleteBy, LocalDateTime.now());

        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
    }

    @DisplayName("답변에 대한 삭제이력을 만들 수 있다")
    @Test
    void create_answer_test() {
        User deleteBy = TestUserFactory.create("서정국");
        DeleteHistory deleteHistory = DeleteHistory.createAnswer(1L, deleteBy, LocalDateTime.now());

        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
