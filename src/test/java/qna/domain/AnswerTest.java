package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("Answer 생성자 생성 성공")
    void constructor_not_exception() {
        assertThatCode(() -> new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Answer 생성자 생성 user객체 null 일시 UnAuthorizedException 발생")
    void constructor_user_null_unauthorized_exception() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents1"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("Answer 생성자 생성 question 객체 null 일시 NotFoundException 발생")
    void constructor_question_null_notfound_exception() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents1"))
                .isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @MethodSource("ownerTestCase")
    @DisplayName("Answer의 작성자 동일 여부 판단 테스트")
    void is_owner_test(Answer answer, User user, boolean expect) {
        assertThat(answer.isOwner(user)).isEqualTo(expect);
    }

    @Test
    @DisplayName("Answer의 Question 변경")
    void change_question() {
        Question question = QuestionTest.Q2;
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer.toQuestion(question);
        assertThat(answer.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("Answer의 delete 상태 변경")
    void change_delete() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer.setDeleted(true);
        assertThat(answer.isDeleted()).isTrue();
    }

    private static Stream<Arguments> ownerTestCase() {
        return Stream.of(
                Arguments.of(A1, UserTest.JAVAJIGI, true),
                Arguments.of(A1, UserTest.SANJIGI, false)
        );
    }
}
