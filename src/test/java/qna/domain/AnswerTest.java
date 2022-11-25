package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

@DisplayName("답변_관련_테스트")
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("User가_answer_삭제하려_할때_성공")
    @Test
    void deleteAnswerWrittenByrUser() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);

        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("다른_User가_answer_삭제하려_할때_실패")
    @Test
    void deleteThrowErrorWhenAnswerWrittenByAnotherUser() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

}
