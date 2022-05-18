package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionRepositoryTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionRepositoryTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("Answer 도메인을 정상적으로 생성할 수 있다.")
    void generate01() {
        // given & when
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionRepositoryTest.Q1, "Contents");

        // then
        assertNotNull(answer);
    }

    @Test
    @DisplayName("Answer 도메인 생성시 user 정보가 없는 경우 UnAuthorizedException이 발생한다.")
    void generate_exception_01() {
        // given & when & then
        assertThatThrownBy(() -> new Answer(null, QuestionRepositoryTest.Q1, "Contents"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("Answer 도메인 생성시 question 정보가 없는 경우 NotFoundException이 발생한다.")
    void generate_exception_02() {
        // given & when & then
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Contents"))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Answer 도메인 생성시 user, question 정보가 둘 다 없는 경우 UnAuthorizedException이 발생한다.")
    void generate_exception_03() {
        // given & when & then
        assertThatThrownBy(() -> new Answer(null, null, "Contents"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("isOnwer 메소드를 이용해 작성자와 동일한지 확인할 수 있다.")
    void public_method_01() {
        // given && when
        User writer = UserTest.JAVAJIGI;

        // then
        assertAll(
            () -> assertTrue(A1.isOwner(writer)),
            () -> assertFalse(A2.isOwner(writer))
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("toQuestion 메소드를 이용해 Answer에 Question을 할당할 수 있다.")
    void public_method_02(long questionId) {
        // given
        Question question = new Question(questionId, "title", "contents");

        // when
        A1.toQuestion(question);

        // then
        assertThat(A1.question()).isEqualTo(question);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("getId 메소드를 이용해 Answer의 id 값을 조회할 수 있다.")
    void public_method_03(long id) {
        // given & when
        Answer answer = new Answer(id, UserTest.JAVAJIGI, QuestionRepositoryTest.Q1, "contents");

        // then
        assertThat(answer.id()).isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("getQuestionId 메소드를 이용해 Question의 id 값을 조회할 수 있다.")
    void public_method_07(long questionId) {
        // given & when
        Question question = new Question(questionId, "title", "contents");
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");

        // then
        assertThat(answer.question()).isEqualTo(question);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    @DisplayName("getContents 메소드를 이용해 Answer의 Contents 값을 조회 할 수 있다.")
    void public_method_09(String contents) {
        // given & when
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionRepositoryTest.Q1, contents);

        // then
        assertTrue(answer.isEqualsContents(contents));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    @DisplayName("setContents 메소드를 이용해 Answer의 Contents 값을 변경 할 수 있다.")
    void public_method_10(String contents) {
        // given & when
        A1.changeContents(contents);

        // then
        assertTrue(A1.isEqualsContents(contents));
    }

    @Test
    @DisplayName("isDeleted 메소드를 이용해 Answer가 삭제 되었는지 확인할 수 있다.")
    void public_method_11() {
        // given & when & then
        assertFalse(A1.isDeleted());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("setDeleted 메소드를 이용해 Answer의 삭제 여부를 변경할 수 있다.")
    void public_method_11(boolean deleted) {
        // given & when
        A1.changeDeleted(deleted);

        // then
        assertThat(A1.isDeleted()).isEqualTo(deleted);
    }
}
