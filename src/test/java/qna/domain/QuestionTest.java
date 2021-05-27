package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", UserTest.SANJIGI);

    @Test
    @DisplayName("본인이 쓴 글이 아니면 삭제가 불가능하다")
    void 본인이_쓴_글이_아니면_삭제가_불가능하다() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q1.delete(UserTest.SANJIGI));
    }

    @Test
    @DisplayName("본인이 쓴 글이면 삭제가 가능하다")
    void 본인이_쓴_글이면_삭제가_가능하다() {
        List<DeleteHistory> deleteHistories = assertDoesNotThrow(() -> Q1.delete(UserTest.JAVAJIGI));

        assertThat(deleteHistories)
                .hasSize(1);
        assertThat(Q1.isDeleted())
                .isTrue();
    }
}
