package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Answer 등록시 Question에 등록된 Answer 조회 테스트")
    public void getAnswers() {
        Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThat(Q1.getAnswers().size()).isEqualTo(1);
        assertThat(Q1.getAnswers()).containsExactly(A1);

        Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
        assertThat(Q1.getAnswers().size()).isEqualTo(2);
        assertThat(Q1.getAnswers()).containsExactly(A1, A2);
    }
}
