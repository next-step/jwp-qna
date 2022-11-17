package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 삭제요청자가_질문작성자이면_질문이삭제된다() throws CannotDeleteException {
        Q1.delete(UserTest.JAVAJIGI);
        Q2.delete(UserTest.SANJIGI);

        assertAll(
            () -> assertThat(Q1.isDeleted()).isEqualTo(true),
            () -> assertThat(Q2.isDeleted()).isEqualTo(true)
        );
    }

    @Test
    void 삭제요청자가_질문작성자가_아니면_에러가_발생한다() {
        assertAll(
            () -> assertThrows(CannotDeleteException.class, () -> Q1.delete(UserTest.SANJIGI)),
            () -> assertThrows(CannotDeleteException.class, () -> Q2.delete(UserTest.JAVAJIGI))
        );
    }
}
