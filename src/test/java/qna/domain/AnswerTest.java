package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 다른유저_답변삭제시_에러() {
        User anotherUser = UserTest.JAVAJIGI;
        assertThatThrownBy(() -> A2.delete(anotherUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    void 답변_삭제시_이력을_반환한다() throws CannotDeleteException {
        User user = UserTest.JAVAJIGI;
        DeleteHistory history = A1.delete(user);
        assertThat(A1.isDeleted()).isTrue();
        assertThat(history.getContentId()).isEqualTo(A1.getId());
    }
}
