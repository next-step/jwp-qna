package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.BlankValidateException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthenticationException;
import qna.exception.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("생성 메서드 유효성 예외 발생")
    @Test
    void createAnswer_exception() throws UnAuthenticationException {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = Question.createQuestion("testTile", "testContents", writer);

        assertThatThrownBy(() -> Answer.createAnswer(writer, question, null))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Answer.createAnswer(writer, question, ""))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Answer.createAnswer(null, question, "testContents"))
                .isInstanceOf(UnAuthorizedException.class);
        assertThatThrownBy(() -> Answer.createAnswer(writer, null, "testContents"))
                .isInstanceOf(NotFoundException.class);
    }
}
