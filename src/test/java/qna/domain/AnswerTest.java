package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {

    @Test
    void writer_is_not_null_test() {
        // given
        User writer = User.create("JAVAJIGI");
        Question question = Question.create(writer);
        // when // then
        assertThatThrownBy(() -> Answer.create(null, question))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void question_is_not_null_test() {
        // given
        User writer = User.create("JAVAJIGI");
        // when then
        assertThatThrownBy(() -> Answer.create(writer, null))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 등록되면 질문에 속하는 답변이 추가 됨")
    void if_made_answer_then_question_answer_add_too() {
        // given
        User writer = User.create("gerrad");
        Question question = Question.create(writer);
        int actual = question.hadNumberOfAnswers();
        // when
        Answer.create(writer, question);
        int expect = question.hadNumberOfAnswers();
        // then
        assertThat(actual + 1).isEqualTo(expect);
    }

    @Test
    @DisplayName("toString 테스트(contents")
    void answer_toString_test() {
        // given
        User writer = User.create("gerrad");
        Question question = Question.create(writer);
        // when
        Answer answer = Answer.create(writer, question);
        System.out.println(answer.toString());
        // then
        assertThat(answer.toString()).contains("contents");

    }
}
