package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.*;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("답변 삭제: 삭제 상태로 변경")
    @Test
    void delete_state() throws UnAuthenticationException, CannotDeleteException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer = Answer.createAnswer(UserTest.JAVAJIGI, question, "testAnswer");
        User deleter = UserTest.JAVAJIGI;

        assertThat(answer.isDeleted()).isFalse();
        answer.delete(deleter);

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제: 답변 작성자가 다른 경우")
    @Test
    void delete_same_user_exception() throws UnAuthenticationException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer = Answer.createAnswer(UserTest.JAVAJIGI, question, "testAnswer");
        User deleter = UserTest.SANJIGI;

        assertThatThrownBy(() -> answer.delete(deleter)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("답변 삭제: 질문자와 답변자가 다른 경우")
    @Test
    void delete_question_writer_answer_writer_exception() throws UnAuthenticationException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer = Answer.createAnswer(UserTest.SANJIGI, question, "testAnswer");
        User deleter = UserTest.SANJIGI;

        assertThatThrownBy(() -> answer.delete(deleter)).isInstanceOf(CannotDeleteException.class);
    }
}
