package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        User writer = UserTest.userSample(1L);
        question = QuestionTest.questionSample(1L, writer);
        answer = AnswerTest.answerSample(1L, writer, question);
    }

    @Test
    @DisplayName("주어진 질문의 삭제 이력을 생성한다")
    void create_delete_history_with_question_test() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);
        assertThat(deleteHistory).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변의 삭제 이력을 생성한다")
    void create_delete_history_with_answer_test() {
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(answer);
        assertThat(deleteHistory).isNotNull();
    }
}
