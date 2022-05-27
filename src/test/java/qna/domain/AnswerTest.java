package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 작성자 동일한지 확인")
    void isOwner() {
        //then
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("질문 변경 후 질문 작성자 id 동일한지 확인")
    void toQuestion() {
        //given
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer contents");

        //when
        answer.toQuestion(QuestionTest.Q2);

        //then
        assertThat(answer.getQuestion()).isEqualTo(QuestionTest.Q2);
    }

    @Test
    @DisplayName("답변 생성시 사용자 null일 경우 Exception 발생 확인")
    void validate_user_null() {
        //then
        assertThatThrownBy(() -> {
            new Answer(null, QuestionTest.Q1, "answer contents");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 생성시 질문 null일 경우 Exception 발생 확인")
    void validate_question_null() {
        //then
        assertThatThrownBy(() -> {
            new Answer(UserTest.JAVAJIGI, null, "answer contents");
        }).isInstanceOf(NotFoundException.class);
    }
}
