package qna.domain;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 작성자가_null일_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() ->
                new Answer(1L, null, QuestionTest.Q1, "test")
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문이_null일_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() ->
                new Answer(1L, UserTest.JAVAJIGI, null, "test")
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 내가_작성한_댓글인지_확인한다() {
        // when
        boolean result = A1.isOwner(UserTest.JAVAJIGI);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void 질문을_설정한다() {
        // given
        Question question = new Question(2L, "title", "contents");
        // when
        A2.toQuestion(question);
        // then
        assertThat(A2.getQuestionId()).isEqualTo(question.getId());
    }
}
