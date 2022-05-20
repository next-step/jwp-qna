package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 질문글의_작성자인지_여부를_확인할_수_있어야_한다() {
        // given
        final User a1Writer = UserTest.JAVAJIGI;
        final User a2Writer = UserTest.SANJIGI;

        // when and then
        assertThat(Q1.isOwner(a1Writer)).isTrue();
        assertThat(Q1.isOwner(a2Writer)).isFalse();

        assertThat(Q2.isOwner(a1Writer)).isFalse();
        assertThat(Q2.isOwner(a2Writer)).isTrue();
    }

    @Test
    void 질문글에_답변을_추가할_수_있어야_한다() {
        // given
        final Question newQuestion = new Question(1L, "title", "contents");
        final Answer newAnswer = AnswerTest.A1;

        // when
        newQuestion.addAnswer(newAnswer);

        // then
        assertThat(newQuestion.getId()).isEqualTo(AnswerTest.A1.getQuestionId());

        // finally
        AnswerTest.A1.toQuestion(QuestionTest.Q1);
    }
}
