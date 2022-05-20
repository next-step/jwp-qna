package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 작성자를_설정한다() {
        Question question = new Question("title", "contents");
        // when
        Question result = question.writeBy(UserTest.SANJIGI);
        // then
        assertThat(result.getWriterId()).isEqualTo(2L);
    }

    @Test
    void 내가_작성한_질문인지_확인한다() {
        // when
        boolean result = Q2.isOwner(UserTest.SANJIGI);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void 답변을_추가한다() {
        // given
        Question question = new Question(3L, "title", "contents");
        Answer answer = new Answer();
        // when
        question.addAnswer(answer);
        // then
        assertThat(answer.getQuestionId()).isEqualTo(3L);
    }
}
