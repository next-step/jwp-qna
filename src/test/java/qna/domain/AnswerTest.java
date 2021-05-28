package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private Question question;
    private Answer writeByJavaJigi;
    private Answer writeBySanjigi;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1", UserTest.JAVAJIGI);

        writeByJavaJigi = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        writeBySanjigi = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    }

    @Test
    @DisplayName("본인이 쓰지않은 댓글이 있으면 CannotDeleteException이 발생하여 삭제가 불가능하다")
    void 본인이_쓰지않은_댓글이_있으면_CannotDeleteException이_발생하여_삭제가_불가능하다() {
        question.addAnswer(writeByJavaJigi);
        question.addAnswer(writeBySanjigi);

        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.delete(UserTest.SANJIGI));
    }

    @Test
    @DisplayName("본인이 쓴 글이면 삭제가 가능하다")
    void 본인이_쓴_글이면_삭제가_가능하다() {
        question.addAnswer(writeByJavaJigi);

        DeleteHistories deleteHistories = assertDoesNotThrow(() -> question.delete(UserTest.JAVAJIGI));

        assertThat(deleteHistories.size())
                .isEqualTo(2);
        assertThat(question.isDeleted())
                .isTrue();
        assertThat(writeByJavaJigi.isDeleted())
                .isTrue();
    }
}
