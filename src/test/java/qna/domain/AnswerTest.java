package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변을 삭제한다")
    void delete() throws CannotDeleteException {
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        DeleteHistory deleteHistory = answer.delete(UserTest.JAVAJIGI);
        DeleteHistory expected = DeleteHistory.ofAnswer(answer.getId(), UserTest.JAVAJIGI);

        assertThat(deleteHistory).isEqualTo(expected);
    }
}
