package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private User writer = new User(1L, "writer1", "password", "name", "email");
    private Question question = new Question(1L, "title", "contents");

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
        assertThat(A2.getQuestion()).isEqualTo(newQuestion);

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

    @Test
    void 작성자가_답변을_삭제하면_답변이_삭제된_상태가_되어야_하고_삭제이력이_반환되어야_한다() throws Exception {
        // given
        final Answer answer = new Answer(1L, writer, question, "contents");

        // when
        final DeleteHistory deleteHistory = answer.delete(writer);

        // then
        assertThat(answer.isDeleted()).isTrue();
        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, 1L, writer, LocalDateTime.now()));
    }
}
