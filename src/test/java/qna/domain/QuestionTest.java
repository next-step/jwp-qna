package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(
        UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Question의 writer가 일치해야 한다.")
    void isOwnerTest() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue();
    }

    @Test
    @DisplayName("Question에 추가된 Answer의 id가 일치해야 한다.")
    void addAnswerTest() {
        // given
        Answer savedAnswer = new Answer(UserTest.JAVAJIGI, Q2, "Answers Contents3");

        // when
        Q2.addAnswer(savedAnswer);

        // then
        assertThat(savedAnswer.getQuestion()).isEqualTo(Q2);
    }

}
