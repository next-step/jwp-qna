package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);


    @Test
    void 작성자_일치_확인() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(Q2.isOwner(UserTest.JAVAJIGI)).isFalse();
    }

    @Test
    void 질문_답변_추가() {
        // given
        Question question = Q1;
        Answer answer = AnswerTest.A1;
        // when
        question.addAnswer(answer);
        // then
        assertThat(answer.getQuestion()).isSameAs(Q1);
    }

    @Test
    void 작성자_업데이트() {
        // given
        Question question = Q1;
        User writer = question.getWriter();
        // when
        question.updateWriter(UserTest.SANJIGI);
        // then
        assertThat(writer.containQuestion(question)).isTrue();
    }
}