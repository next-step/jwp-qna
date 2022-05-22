package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void isOwner() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse();
    }
}
