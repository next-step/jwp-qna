package qna.domain.question;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import qna.domain.User;
import qna.domain.UserTest;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.exception.question.QuestionOwnerNotMatchedException;

public class QuestionTest {
    public static final Question Q1 = new Question(UserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(UserTest.SANJIGI, "title2", "contents2");

    @Test
    @DisplayName("질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.")
    void when_deleteQuestion_then_deletedTrue() throws QuestionOwnerNotMatchedException, AnswerOwnerNotMatchedException {
        Question question = new Question(UserTest.SANJIGI, "title", "content");
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        question.deleteBy(UserTest.SANJIGI);
        assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
    }

    @ParameterizedTest
    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다")
    @MethodSource("loginUserAttemptToDeleteDataSource")
    void when_loginUserAttemptToDelete_then_succeed(User loginUser, User writer, boolean succeed) throws
            AnswerOwnerNotMatchedException, QuestionOwnerNotMatchedException {
        Question question = new Question(writer, "title", "content");
        assertThat(question).hasFieldOrPropertyWithValue("deleted", false);
        if (succeed) {
            question.deleteBy(UserTest.SANJIGI);
            assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
            return;
        }
        assertThatThrownBy(() -> question.deleteBy(loginUser))
            .isInstanceOf(QuestionOwnerNotMatchedException.class);
    }

    static Stream<Arguments> loginUserAttemptToDeleteDataSource() {
        return Stream.of(
            Arguments.of(UserTest.SANJIGI, UserTest.SANJIGI, true),
            Arguments.of(UserTest.SANJIGI, UserTest.JAVAJIGI, false)
        );
    }
}
