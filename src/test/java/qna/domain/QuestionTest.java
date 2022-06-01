package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자 동일한지 확인")
    void isOwner() {
        //then
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("답변 추가 후 질문자 id로 변경되었지 확인")
    void addAnswer() {
        //given
        Answer answer = AnswerTest.A2;
        Question question = Q1;

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
            Q1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제시 삭제여부 true 로 변경되는지 확인")
    void delete() throws CannotDeleteException {
        //given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        //when
        question.delete(UserTest.JAVAJIGI);

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변 없는 질문 삭제시 삭제기록 확인")
    void delete_no_answer() throws CannotDeleteException {
        //given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        //then
        assertThat(deleteHistories).hasSize(1);
    }

    @Test
    @DisplayName("질문 삭제시 답변도 같이 삭제되는지 확인")
    void delete_with_answer() throws CannotDeleteException {
        //given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        //then
        for (Answer a : question.getAnswers()) {
            assertThat(a.isDeleted()).isTrue();
        }
        assertThat(deleteHistories).hasSize(2);
    }

    @Test
    @DisplayName("다른 사용자 답변이 삭제된 경우 질문 삭제 가능한지 확인")
    void delete_with_other_answer() throws CannotDeleteException {
        //given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.SANJIGI, question, "Answers Contents1");
        answer.delete(answer.getWriter());
        question.addAnswer(answer);

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        //then
        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories).hasSize(1);
    }
}
