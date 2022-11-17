package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.fixtures.AnswerTestFixture;
import qna.fixtures.QuestionTestFixture;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    @Test
    @DisplayName("주어진 질문의 삭제 이력을 생성한다")
    void create_delete_history_with_question_test() {
        Question question = QuestionTestFixture.createQuestionWithId(1L);
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);
        assertThat(deleteHistory).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변의 삭제 이력을 생성한다")
    void create_delete_history_with_answer_test() {
        Answer answer = AnswerTestFixture.createAnswerWithId(1L);
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(answer);
        assertThat(deleteHistory).isNotNull();
    }
}
