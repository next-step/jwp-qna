package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변삭제시_질문자와_다를경우_예외발생() {
        User loginUser = UserTest.HOONHEE;
        assertThatThrownBy(() -> A1.delete(loginUser))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 삭제시_삭제상태가_변경되는지_확인() {
        User loginUser = UserTest.JAVAJIGI;
        A1.delete(loginUser);
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    void 삭제시_삭제이력을_반환하는지_확인() {
        //TODO - 답변삭제시 삭제 이력을 반환해야 한다.
        User loginUser = UserTest.JAVAJIGI;
        DeleteHistory deleteHistory = A1.delete(loginUser);
        assertThat(deleteHistory).isNotNull();
    }

}
