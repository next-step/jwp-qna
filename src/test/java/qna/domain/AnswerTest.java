package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변의_작성자인지_여부를_확인할_수_있어야_한다() {
        // given
        final User a1Writer = UserTest.JAVAJIGI;
        final User a2Writer = UserTest.SANJIGI;

        // when and then
        assertThat(A1.isOwner(a1Writer)).isTrue();
        assertThat(A1.isOwner(a2Writer)).isFalse();

        assertThat(A2.isOwner(a1Writer)).isFalse();
        assertThat(A2.isOwner(a2Writer)).isTrue();
    }

    @Test
    void 답변의_질문글을_변경할_수_있어야_한다() {
        // given
        final Question newQuestion = QuestionTest.Q2;

        // when
        A2.toQuestion(newQuestion);

        // then
        assertThat(A2.getQuestionId()).isEqualTo(newQuestion.getId());

        // finally
        A2.toQuestion(QuestionTest.Q1);
    }

    @Test
    void 답변의_삭제_여부를_설정할_수_있어야_한다() {
        // given
        final boolean original = A1.isDeleted();

        // when
        A1.setDeleted(!A1.isDeleted());

        // then
        assertThat(A1.isDeleted()).isNotEqualTo(original);

        // finally
        A1.setDeleted(original);
    }
}
