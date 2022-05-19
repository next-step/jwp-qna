package qna.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.createQuestion;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void delete() {
        Answer answer = createAnswer();
        assertThat(answer.isDeleted()).isFalse();
        assertThat(answer.delete()).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        assertThat(answer.isDeleted()).isTrue();
    }

    private Answer createAnswer() {
        return new Answer(1L, UserTest.JAVAJIGI, createQuestion(UserTest.JAVAJIGI, false), "answer content1");
    }
}
