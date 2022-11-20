package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변을 삭제할때 사용자가 null 일경우에 에러를 던진다.")
    void givenNull_whenDelete_thenThrow() {
        QuestionTest.Q1.addAnswer(A1);
        assertThatThrownBy(() -> A1.delete(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("삭제요청자가 null 입니다.");
    }

    @Test
    @DisplayName("답변을 삭제할때 질문,답변 작성자가 다를경우 에러를 던진다.")
    void givenOtherAnswerUser_whenDelete_thenThrow() {
        QuestionTest.Q1.addAnswer(A1);
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문작성자와 답변작성자는 서로 다릅니다.");
    }
}
