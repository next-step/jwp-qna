package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    @Test
    @DisplayName("질문 작성자 동일한지 확인")
    void isOwner() {
        //then
        assertAll(
                () -> assertThat(QuestionFixtures.Q1.isOwner(UserFixtures.JAVAJIGI)).isTrue(),
                () -> assertThat(QuestionFixtures.Q2.isOwner(UserFixtures.SANJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("답변 추가 후 질문자 id로 변경되었지 확인")
    void addAnswer() {
        //given
        Answer answer = AnswerFixtures.A2;
        Question question = QuestionFixtures.Q1;

        //when
        question.addAnswer(answer);

        //then
        assertThat(answer.getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("로그인 사용자와 질문자가 다를 경우 Exception 발생 확인")
    void validateDelete() {
        //then
        assertThatThrownBy(() -> {
            QuestionFixtures.Q1.delete(UserFixtures.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제시 삭제여부 true 로 변경되는지 확인")
    void delete() throws CannotDeleteException {
        //given
        Question question = QuestionFixtures.createByUser("title", "content", UserFixtures.JAVAJIGI);

        //when
        question.delete(UserFixtures.JAVAJIGI);

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변 없는 질문 삭제시 삭제기록 확인")
    void delete_no_answer() throws CannotDeleteException {
        //given
        Question question = QuestionFixtures.createByUser("title", "content", UserFixtures.JAVAJIGI);

        //when
        DeleteHistories deleteHistories = question.delete(UserFixtures.JAVAJIGI);

        //then
        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    @DisplayName("질문 삭제시 답변도 같이 삭제되는지 확인")
    void delete_with_answer() throws CannotDeleteException {
        //given
        Question question = QuestionFixtures.createByUser("title", "content", UserFixtures.JAVAJIGI);
        Answer answer = AnswerFixtures.createDefault(UserFixtures.JAVAJIGI, question);
        question.addAnswer(answer);

        //when
        DeleteHistories deleteHistories = question.delete(UserFixtures.JAVAJIGI);

        //then
        for (Answer a : question.getAnswers()) {
            assertThat(a.isDeleted()).isTrue();
        }
        assertThat(deleteHistories.getDeleteHistories()).hasSize(2);
    }

    @Test
    @DisplayName("다른 사용자 답변이 삭제된 경우 질문 삭제 가능한지 확인")
    void delete_with_other_answer() throws CannotDeleteException {
        //given
        Question question = QuestionFixtures.createByUser("title", "content", UserFixtures.JAVAJIGI);
        Answer answer = AnswerFixtures.createDefault(UserFixtures.SANJIGI, question);
        answer.delete(answer.getWriter());
        question.addAnswer(answer);

        //when
        DeleteHistories deleteHistories = question.delete(UserFixtures.JAVAJIGI);

        //then
        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }
}
