package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;;

@DisplayName("Answer 테스트")
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    public static final Answer DELETED_ANSWER1 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Deleted Content1");
    public static final Answer DELETED_ANSWER2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Deleted Content2");
    public static final Answer DELETED_ANSWER3 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Deleted Content3");

    @Test
    @DisplayName("delete_정상")
    void delete_정상() throws CannotDeleteException {
        // Given
        Answer target = A1;

        // When
        DeleteHistory deleteHistory = target.delete();

        // Then
        assertThat(deleteHistory.isAnswerType()).isTrue();
        assertThat(deleteHistory.getId()).isEqualTo(target.getId());
        assertThat(deleteHistory.isWriter(target.getWriter())).isTrue();
    }
}
