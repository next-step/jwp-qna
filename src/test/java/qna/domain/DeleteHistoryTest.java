package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.fixtures.AnswerTestFixture;
import qna.fixtures.QuestionTestFixture;
import qna.fixtures.UserTestFixture;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question(1L, UserTestFixture.손상훈, "title", "contents");
    }

    @Test
    @DisplayName("주어진 질문의 삭제 이력을 생성한다")
    void create_delete_history_with_question_test() {
        // when
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);
        // then
        assertThat(deleteHistory).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변의 삭제 이력을 생성한다")
    void create_delete_history_with_answer_test() {
        // given
        Answer answer = new Answer(1L, UserTestFixture.손상훈, question, "contents");
        // when
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(answer);
        // then
        assertThat(deleteHistory).isNotNull();
    }
}
