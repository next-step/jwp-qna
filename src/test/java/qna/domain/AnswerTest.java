package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("다른사람이 삭제요청한 경우 exception")
    public void deleteByOther() throws CannotDeleteException {
        assertThatThrownBy(()->A1.delete(UserTest.SANJIGI))
        .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("작성자가 같다면 삭제")
    public void delete() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
        assertThat(UserTest.JAVAJIGI.getDeleteHistories())
                .contains(new DeleteHistory(ContentType.ANSWER,A1.getId(),A1.getWriter(), LocalDateTime.now()));
    }
}
