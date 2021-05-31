package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.BlankValidateException;
import qna.exception.CannotDeleteException;
import qna.exception.UnAuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("생성 메서드 유효성 예외 발생")
    @Test
    void createQuestion_exception() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");

        assertThatThrownBy(() -> Question.createQuestion(null, "testContents", writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("", "testContents", writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("testTile", null, writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("testTile", "", writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("testTile", "testContents", null))
                .isInstanceOf(UnAuthenticationException.class);
    }

    @DisplayName("질문 삭제: 삭제 상태로 변경")
    @Test
    void delete_state() throws UnAuthenticationException, CannotDeleteException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        User deleter = UserTest.JAVAJIGI;

        assertThat(question.isDeleted()).isFalse();
        question.delete(deleter);

        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문 삭제: 질문 작성자가 다른 경우")
    @Test
    void delete_same_user_exception() throws UnAuthenticationException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        User deleter = UserTest.SANJIGI;

        assertThatThrownBy(() -> question.delete(deleter)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문 삭제: 답변작성자가 다른 경우")
    @Test
    void delete_question_answer_same_user_exception() throws UnAuthenticationException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer1 = AnswerTest.A1;
        Answer answer2 = AnswerTest.A2;
        User deleter = UserTest.JAVAJIGI;

        answer1.toQuestion(question);
        answer2.toQuestion(question);

        assertThatThrownBy(() -> question.delete(deleter)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문 삭제: 답변작성자가 질문작성자와 모두 같은 경우")
    @Test
    void delete_question_answer_same_user() throws UnAuthenticationException, CannotDeleteException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer1 = AnswerTest.A1;
        User deleter = UserTest.JAVAJIGI;
        answer1.toQuestion(question);

        question.delete(deleter);

        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문 삭제: 답변이 없는 경우")
    @Test
    void delete_question_no_answer_user() throws UnAuthenticationException, CannotDeleteException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        User deleter = UserTest.JAVAJIGI;

        question.delete(deleter);

        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문 삭제: 질문 삭제시 답변도 삭제되야한다.")
    @Test
    void delete_question_with_answers() throws UnAuthenticationException, CannotDeleteException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer1 = Answer.createAnswer(UserTest.JAVAJIGI, question, "testContents");
        Answer answer2 = Answer.createAnswer(UserTest.JAVAJIGI, question, "testContents");
        User deleter = UserTest.JAVAJIGI;

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer1.isDeleted()).isFalse();
        assertThat(answer2.isDeleted()).isFalse();
        question.delete(deleter);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }

    @DisplayName("질문 삭제: 삭제 히스토리 남기기")
    void delete_question_history() throws UnAuthenticationException {
        Question question = Question.createQuestion("testTitle", "testContents", UserTest.JAVAJIGI);
        User deleter = UserTest.JAVAJIGI;

        question.delete(deleter);

        assertThat(question.isDeleted()).isTrue();
    }
}
