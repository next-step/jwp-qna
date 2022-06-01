package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

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
}
