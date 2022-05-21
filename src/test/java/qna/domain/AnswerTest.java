package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.SANJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("Answer 작성작가 아닌 다른 사람이 삭제시 예외 처리")
    void deleteTest() {
        assertThatThrownBy(
                () -> A1.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
