package qna.domain.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.history.DeleteHistory;
import qna.domain.question.QuestionTest;
import qna.domain.user.UserTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");


    @Test
    @DisplayName("Answer를 삭제했을때 변하는 상태값과 리턴값인 DeleteHistory 테스트")
    void deleteTest() {
        // when
        DeleteHistory deleteHistory = A1.delete();

        // then
        assertThat(deleteHistory)
            .isNotNull()
            .extracting(value -> value.isSameOwner(UserTest.JAVAJIGI))
            .isEqualTo(true);
        assertThat(A1.isDeleted()).isTrue();
    }
}
