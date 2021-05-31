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
    @DisplayName("작성자와 다른 사용자가 삭제시 예외가 발생한다")
    void deleteNotOwnerExceptionTest() {
        assertThatThrownBy(() -> A1.deleteByOwner(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 시 deleted : true 변경, contentType : Answer 인 DeleteHistory 를 반환한다")
    void deleteTest() throws CannotDeleteException {
        DeleteHistory deleteHistory = A1.deleteByOwner(UserTest.JAVAJIGI);

        assertThat(A1.isDeleted()).isEqualTo(true);
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
