package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer ANSWER = new Answer(1L, UserTest.JAVAJIGI_WITH_ID, QuestionTest.Q1, "Answers Contents");

    @Test
    void 다른_사람이_작성한_답변은_삭제할_수_없다() {
        User anotherUser = UserTest.SANJIGI_WITH_ID;
        assertThatThrownBy(() -> ANSWER.delete(anotherUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    void 답변_삭제시_삭제_이력을_반환한다() throws CannotDeleteException {
        User user = UserTest.JAVAJIGI_WITH_ID;
        DeleteHistory history = ANSWER.delete(user);
        assertThat(ANSWER.isDeleted()).isTrue();
        assertThat(history.getDeleteByUser()).isEqualTo(user);
        assertThat(history.getContentId()).isEqualTo(ANSWER.getId());
    }
}
