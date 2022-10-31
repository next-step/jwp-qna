package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    
    @Test
    void Answer_contents_test() {
        assertThat(A1.getContents()).isEqualTo("Answers Contents1");
    }
    @Test
    void Answer_questionId_test() {
        assertThat(A1.getQuestionId()).isEqualTo(A2.getQuestionId());
    }

    @Test
    void Answer_owner_test() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    void Answer_isDeleted_test() {
        assertThat(A1.isDeleted()).isEqualTo(A2.isDeleted());
    }
}
