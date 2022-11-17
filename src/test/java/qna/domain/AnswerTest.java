package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 삭제요청자가_답변작성자이면_답변이삭제된다() {
        A1.delete(UserTest.JAVAJIGI);
        A2.delete(UserTest.SANJIGI);

        assertAll(
            () -> assertThat(A1.isDeleted()).isEqualTo(true),
            () -> assertThat(A2.isDeleted()).isEqualTo(true)
        );
    }

    @Test
    void 삭제요청자가_답변작성자가_아니면_에러가_발생한다() {
        assertAll(
            () -> assertThrows(CannotDeleteException.class, () -> A1.delete(UserTest.SANJIGI)),
            () -> assertThrows(CannotDeleteException.class, () -> A2.delete(UserTest.JAVAJIGI))
        );
    }
}
