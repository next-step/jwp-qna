package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    public static final Answer A3 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Content3");
    public static final Answer A4 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents4");
    public static final Answer A5 = new Answer(UserTest.HONGHEE, QuestionTest.Q5, "Answers Contents4");


    @DisplayName("정상적으로 삭제가 되는 경우 테스트")
    @Test
    public void deleteAnswerTest() throws CannotDeleteException {

        // when
        User loginUser = UserTest.SANJIGI;
        Answer answer = A2;

        // then
        DeleteHistory deleteHistory = answer.deleteAnswer(loginUser);

        // given
        Assertions.assertThat(answer.isDeleted()).isTrue();
        Assertions.assertThat(deleteHistory).isNotNull();
    }

    @DisplayName("정상적으로 삭제되지 않는 경우 테스트")
    @Test
    public void deleteAnswerFailureTest() {

        // when
        User loginUser = UserTest.SANJIGI;
        Answer answer = A1;

        // then
        Assertions.assertThatThrownBy(() -> {
            DeleteHistory deleteHistory = answer.deleteAnswer(loginUser);
            Assertions.assertThat(deleteHistory).isNull();
        });

        // given
        Assertions.assertThat(answer.isDeleted()).isFalse();
    }
}
