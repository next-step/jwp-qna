package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    public static final Answer A3 = new Answer(UserTest.SANJIGI, QuestionTest.Q3, "testContents");
    public static final Answer A4 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q3, "testContents");

    @Test
    @DisplayName("자신이 작성한 답변을 삭제상태로 변경한다.")
    void delete_answer() throws CannotDeleteException {
        assertThat(A1.isDeleted()).isFalse();
        A1.delete(UserTest.JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("본인이 작성하지 않은 답변은 삭제할 수 없다.")
    void answer_by_other_writers_not_deletable() {
        assertThatThrownBy(() -> A2.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }


}
