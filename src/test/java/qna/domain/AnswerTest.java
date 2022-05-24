package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("Owner 확인")
    @Test
    void isOwner() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @DisplayName("답변자는 답변을 삭제할 수 있다.")
    @Test
    void valid_delete() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("답변자는 다른 답변자의 답변을 삭제할 수 없다.")
    @Test
    void invalid_delete() {
        assertThatThrownBy(() -> A2.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
