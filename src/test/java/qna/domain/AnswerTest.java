package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    public static final Answer A3 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");
    public static final Answer A4 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");

    @Test
    @DisplayName("질문자와 답변자가 동일한 경우 삭제 가능")
    void delete() {
        A1.delete(UserTest.JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 다를 경우 삭제 불가")
    void deleteFailedUser() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 이력을 반환")
    void returnDeleteHistory() {
        DeleteHistory history = A1.delete(UserTest.JAVAJIGI);

        assertThat(history).isNotNull();
        assertThat(history.getContentType()).isEqualTo(ContentType.ANSWER);
        assertThat(history.getDeletedBy()).isEqualTo(UserTest.JAVAJIGI);
    }
}
