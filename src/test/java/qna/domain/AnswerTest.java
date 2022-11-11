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
        User writer = TestUserFactory.create("JAVAJIGI");
        Question question = TestQuestionFactory.create(writer);
        // when // then
        assertThatThrownBy(() -> new Answer(null, question, "content"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void question_is_not_null_test() {
        // given
        User writer = TestUserFactory.create("JAVAJIGI");
        // when then
        assertThatThrownBy(() -> new Answer(writer, null, "content"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 등록되면 질문에 속하는 답변이 추가 됨")
    void if_made_answer_then_question_answer_add_too() {
        // given
        User writer = TestUserFactory.create("gerrad");
        Question question = TestQuestionFactory.create(writer);
        int actual = question.getAnswers().size();
        // when
        TestAnswerFactory.create(writer, question);
        int expect = question.getAnswers().size();
        // then
        assertThat(actual + 1).isEqualTo(expect);
    }
}
