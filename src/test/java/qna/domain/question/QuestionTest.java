package qna.domain.question;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.User;
import qna.domain.UserTest;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.exception.question.QuestionOwnerNotMatchedException;

public class QuestionTest {
    public static final Question Q1 = new Question(UserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(UserTest.SANJIGI, "title2", "contents2");

    Question question;

    User questionWriter;

    @BeforeEach
    void beforeAll() {
        questionWriter = UserTest.SANJIGI;
        question = new Question(questionWriter, "title", "content");
    }

    @Test
    @DisplayName("질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.")
    void when_deleteQuestion_then_deletedTrue() throws QuestionOwnerNotMatchedException, AnswerOwnerNotMatchedException {
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        question.deleteBy(UserTest.SANJIGI);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
    }

    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다")
    void when_loginUserAttemptToDelete_then_succeed() throws
            AnswerOwnerNotMatchedException, QuestionOwnerNotMatchedException {
        User loginUser = UserTest.SANJIGI;
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        question.deleteBy(loginUser);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
    }

    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 다른 경우 삭제할 수 없다")
    void when_loginUserAttemptToDelete_then_failed() {
        User loginUser = UserTest.JAVAJIGI;
        assertThat(loginUser).isNotEqualTo(questionWriter);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        assertThatThrownBy(() -> question.deleteBy(loginUser))
            .isInstanceOf(QuestionOwnerNotMatchedException.class);
    }

    @Test
    @DisplayName("답변이 없는 경우 삭제할 수 있다.")
    void when_noAnswers_then_succeed() throws QuestionOwnerNotMatchedException, AnswerOwnerNotMatchedException {
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        question.deleteBy(UserTest.SANJIGI);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
    }

    @Test
    @DisplayName("답변자가 자신만 있는 경우 있는 경우 삭제할 수 있다.")
    void when_answersOnlyWriter_then_succeed() throws QuestionOwnerNotMatchedException, AnswerOwnerNotMatchedException {
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);

        User answerUser = UserTest.SANJIGI;
        question.addAnswer(answerUser, "answer");

        question.deleteBy(questionWriter);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
    }

    @Test
    @DisplayName("답변자가 자신 이외에 다른 사용자가 있는 경우 있는 경우 삭제할 수 없다.")
    void when_existAnotherUserAnswers_then_failed() {
        User questionWriter = UserTest.SANJIGI;
        Question question = new Question(questionWriter, "title", "content");
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);

        User answerUser = UserTest.JAVAJIGI;
        question.addAnswer(answerUser, "answer");

        assertThatThrownBy(() -> question.deleteBy(questionWriter))
            .isInstanceOf(AnswerOwnerNotMatchedException.class);
    }
}
