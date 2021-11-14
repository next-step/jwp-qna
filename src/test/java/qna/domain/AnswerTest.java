package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.answer.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

    @Test
    @DisplayName("답변 생성 성공")
    public void createAnswerTest() {
        Answer actual = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        assertThat(actual).isEqualTo(A1);
    }

    @Test
    @DisplayName("답변 생성 실패 - null user")
    public void createAnswerTest_nullUser() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents")).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 생성 실패 - null question")
    public void createAnswerTest_nullQuestion() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents")).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 삭제상태로 변경")
    public void answerDeletedStateTest() {
        assertThat(A1.isDeleted()).isFalse();
        A1.deleteAnswer();
        assertThat(A1.isDeleted()).isTrue();
    }
}
