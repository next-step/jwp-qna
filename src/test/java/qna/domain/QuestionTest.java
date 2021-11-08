package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import qna.CannotDeleteException;

@DisplayName("질문")
public class QuestionTest {

    public static final Question Q1 = Question.of("title1", "contents1");

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Question.of("title", "Contents"));
    }

    @ParameterizedTest(name = "{displayName}[{index}] if title is {0}, can not be instanced")
    @DisplayName("제목이 비어있는 상태로 객체화하면 IllegalArgumentException")
    @NullAndEmptySource
    void instance_emptyTitle_thrownIllegalArgumentException(String title) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Question.of(title, "contents"))
            .withMessage("'title' must not be empty");
    }

    @Test
    @DisplayName("답변 추가")
    void addAnswer() {
        //given
        Question question = Question.of("title", "contents");

        //when
        question.addAnswer(AnswerTest.A1);

        //then
        assertAll(
            () -> assertThat(question.containsAnswer(AnswerTest.A1)).isTrue(),
            () -> assertThat(question.containsAnswer(AnswerTest.A2)).isFalse()
        );
    }

    @Test
    @DisplayName("null 답변 추가하면 IllegalArgumentException")
    void addAnswer_nullAnswer_thrownIllegalArgumentException() {
        //given
        Question question = Question.of("title", "contents");

        //when
        ThrowingCallable addAnswerCall = () -> question.addAnswer(null);

        //then
        assertThatIllegalArgumentException()
            .isThrownBy(addAnswerCall)
            .withMessage("added 'answer' must not be null");
    }

    @Test
    @DisplayName("삭제")
    void delete() throws CannotDeleteException {
        //given
        Question question = Question.of("title", "contents")
            .writeBy(UserTest.JAVAJIGI);
        Answer answer = Answer.of(UserTest.JAVAJIGI, question, "contents");
        question.addAnswer(answer);

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        //then
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(answer.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories)
                .hasSize(2)
                .extracting("contentType", "deletedByUser")
                .contains(
                    tuple(ContentType.QUESTION, UserTest.JAVAJIGI),
                    tuple(ContentType.ANSWER, UserTest.JAVAJIGI)
                )
        );
    }

    @Test
    @DisplayName("본인의 질문이 아닌 경우 삭제하면 CannotDeleteException")
    void delete_notOwn_thrownCannotDeleteException() {
        //given
        Question question = Question.of("title", "contents")
            .writeBy(UserTest.JAVAJIGI);
        Answer answer = Answer.of(UserTest.JAVAJIGI, question, "contents");
        question.addAnswer(answer);

        //when
        ThrowingCallable deleteCall = () -> question.delete(UserTest.SANJIGI);

        //then
        assertAll(
            () -> assertThat(question.isDeleted()).isFalse(),
            () -> assertThat(answer.isDeleted()).isFalse(),
            () -> assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(deleteCall)
                .withMessageContaining("질문을 삭제할 권한이 없습니다.")
        );
    }

    @Test
    @DisplayName("다른 사람의 답변이 있는 경우 삭제하면 CannotDeleteException")
    void delete_containsOtherUsersAnswer_thrownCannotDeleteException() {
        //given
        Question question = Question.of("title", "contents")
            .writeBy(UserTest.JAVAJIGI);
        Answer answer = Answer.of(UserTest.SANJIGI, question, "contents");
        question.addAnswer(answer);

        //when
        ThrowingCallable deleteCall = () -> question.delete(UserTest.JAVAJIGI);

        //then
        assertAll(
            () -> assertThat(question.isDeleted()).isFalse(),
            () -> assertThat(answer.isDeleted()).isFalse(),
            () -> assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(deleteCall)
                .withMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.")
        );
    }
}
