package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1", UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("본인이 쓴 글이 아니면 CannotDeleteException가 발생하여 삭제가 불가능하다")
    void 본인이_쓴_글이_아니면_CannotDeleteException가_발생하여_삭제가_불가능하다() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.delete(UserTest.SANJIGI));
        assertThat(question.isDeleted())
                .isFalse();
    }

    @Test
    @DisplayName("본인이 쓴 글이면 삭제가 가능하다")
    void 본인이_쓴_글이면_삭제가_가능하다() {
        Question question = new Question("title1", "contents1", UserTest.JAVAJIGI);

        DeleteHistories deleteHistories = assertDoesNotThrow(() -> question.delete(UserTest.JAVAJIGI));

        assertThat(deleteHistories.size())
                .isEqualTo(1);
        assertThat(question.isDeleted())
                .isTrue();
    }
}
