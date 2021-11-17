package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.common.exception.CannotDeleteException;
import qna.common.exception.NotFoundException;
import qna.common.exception.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

    @Test
    @DisplayName("Answer 의 deleted 컬럼의 default 값은 false입니다.")
    void deleted_기본값_false() {
        // when
        boolean expect = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
            "Answers Contents2").isDeleted();

        // then
        assertThat(expect).isFalse();
    }

    @Test
    @DisplayName("답변 작성자가 답변 삭제 처리, 결과 true")
    void deleted() {
        // given
        Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

        // when
        answer.delete(UserTest.SANJIGI);

        // then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변 작성자가 아닌 User 가 삭제 처리, 결과 false")
    void deleted_false() {
        // given
        Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

        assertThatExceptionOfType(CannotDeleteException.class) // then
            .isThrownBy(() -> {
                // when
                answer.delete(UserTest.JAVAJIGI);
            });

    }

    @Test
    @DisplayName("질문 writer(작성자) 검증")
    void isOwner_질문_작성자_확인() {

        // then
        assertAll(
            () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("저장시 writer(작성자)가 없으면 예외 발생")
    void exception_create_User_null() {
        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                new Answer(null, QuestionTest.Q1, "Answers Contents1");
            });
    }

    @Test
    @DisplayName("저장시 question(질문)이 없으면 예외 발생")
    void exception_create_Question_null() {
        assertThatExceptionOfType(NotFoundException.class) // then
            .isThrownBy(() -> {
                // when
                new Answer(UserTest.JAVAJIGI, null, "Answers Contents1");
            });
    }

    @Test
    @DisplayName("게스트는 답변 할 수 없습니다. 예외 발생")
    void guest_answer_fail() {
        // given
        User guest = User.GUEST_USER;

        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                new Answer(guest, QuestionTest.Q1, "Answers Contents1");
            });
    }
}
