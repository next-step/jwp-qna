package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = User.create("JAVAJIGI", "1234", "JAVAJIGI", "JAVAJIGI@gmail.com");
        question = Question.create("title", "contents", writer);
    }
    @Test
    void writer_is_not_null_test() {
        // when // then
        assertThatThrownBy(() -> Answer.create(null, question, ""))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void question_is_not_null_test() {
        // when then
        assertThatThrownBy(() -> Answer.create(writer, null, ""))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 등록되면 질문에 속하는 답변이 추가 됨")
    void if_made_answer_then_question_answer_add_too() {
        // given
        int actual = question.hadNumberOfAnswers();
        // when
        Answer.create(writer, question, "");
        int expect = question.hadNumberOfAnswers();
        // then
        assertThat(actual + 1).isEqualTo(expect);
    }

    @Test
    @DisplayName("toString 테스트(contents")
    void answer_toString_test() {
        // given
        // when
        Answer answer = Answer.create(writer, question, "contents");
        System.out.println(answer.toString());
        // then
        assertThat(answer.toString()).contains("contents");

    }
}
