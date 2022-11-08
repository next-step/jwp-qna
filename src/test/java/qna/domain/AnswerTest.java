package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.JAVAJIGI;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {

    public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

    @Test
    void 작성자가_작성한_항목이_아니면_삭제불가능() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void 답변_삭제시_삭제기록_반환() {
        DeleteHistory history = A1.delete(JAVAJIGI);
        assertThat(history)
            .isEqualTo(new DeleteHistory(ContentType.ANSWER, A1.getId(), JAVAJIGI.getId(), null));
    }

    @Test
    void 답변_삭제() {
        A1.delete(JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
    }
}
