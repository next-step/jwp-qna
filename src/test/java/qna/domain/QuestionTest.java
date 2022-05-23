package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("답변이 없는 경우 삭제가 가능하다.")
    void successDeleteNoAnswer() {
        Question q1 = new Question("답변없음", "답변없음").writeBy(UserTest.SANJIGI);
        Question q2 = new Question("답변있음", "답변있음").writeBy(UserTest.SANJIGI);
        q2.addAnswer(AnswerTest.A1);

        assertAll(
            ()-> assertThat(q1.isAbleDelete()).isTrue(),
            ()-> assertThat(q2.isAbleDelete()).isFalse()
        );
    }

    @Test
    @DisplayName("답변이 없는 경우 삭제가 가능하다.")
    void successDeleteNoAnswer2() {

    }
}
