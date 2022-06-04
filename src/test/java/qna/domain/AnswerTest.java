package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    @Test
    @DisplayName("답변 작성자 동일한지 확인")
    void isOwner() {
        //then
        assertAll(
                () -> assertThat(AnswerFixtures.A1.isOwner(UserFixtures.JAVAJIGI)).isTrue(),
                () -> assertThat(AnswerFixtures.A2.isOwner(UserFixtures.SANJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("질문 변경 후 질문 작성자 id 동일한지 확인")
    void toQuestion() {
        //given
        Answer answer = AnswerFixtures.A1;

        //when
        answer.toQuestion(QuestionFixtures.Q2);

        //then
        assertThat(answer.getQuestion()).isEqualTo(QuestionFixtures.Q2);
    }

    @Test
    @DisplayName("답변 생성시 사용자 null일 경우 Exception 발생 확인")
    void validate_user_null() {
        //then
        assertThatThrownBy(() -> {
            new Answer(null, QuestionFixtures.Q1, "answer contents");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 생성시 질문 null일 경우 Exception 발생 확인")
    void validate_question_null() {
        //then
        assertThatThrownBy(() -> {
            new Answer(UserFixtures.JAVAJIGI, null, "answer contents");
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 삭제시 로그인 사용자와 답변자가 다를 경우 Exception 발생 확인")
    void validateDelete() {
        //given
        Answer answer = AnswerFixtures.A1;

        //then
        assertThatThrownBy(() -> {
            answer.delete(UserFixtures.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변 삭제시 삭제여부 true 로 변경되는지 확인")
    void delete() throws CannotDeleteException {
        //given
        Answer answer = AnswerFixtures.A1;

        //then
        assertThat(answer.isDeleted()).isFalse();

        //when
        answer.delete(UserFixtures.JAVAJIGI);

        //then
        assertThat(answer.isDeleted()).isTrue();
    }
}
