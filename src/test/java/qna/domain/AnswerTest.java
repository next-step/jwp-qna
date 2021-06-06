package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void deleteAnswer() {
        //when
        DeleteHistory actual = A1.delete(UserTest.JAVAJIGI);

        //then
        assertThat(actual.getDeletedBy()).isSameAs(UserTest.JAVAJIGI);
    }

    @DisplayName("답변은 만든사람만이 삭제할 수 있다.")
    @Test
    void deleteAnswerException() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContainingAll(Answer.DELETE_EXCEPTION_MESSAGE);
    }
}
