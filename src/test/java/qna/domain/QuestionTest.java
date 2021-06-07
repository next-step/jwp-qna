package qna.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Question 테스트")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static final Question DELETED_QUESTION1 = new Question("deleted question title1", "deleted question content1").writeBy(UserTest.SANJIGI);
    public static final Question DELETED_QUESTION2 = new Question("deleted question title2", "deleted question content2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("delete_정상")
    void delete_정상() throws CannotDeleteException {
        // Given
        Question target = Q1;

        // When
        DeleteHistory deleteHistory = target.delete();

        // Then
        assertThat(deleteHistory.isQuestionType()).isTrue();
        assertThat(deleteHistory.getId()).isEqualTo(target.getId());
        assertThat(deleteHistory.isWriter(target.getWriter())).isTrue();
    }
}
