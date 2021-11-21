package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    public static final Answer A3 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents3");
    public static final Answer A4 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents4");

    @DisplayName("작성자 일치")
    @Test
    void validateOwner_true() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("작성자 불일치")
    @Test
    void validateOwner_false() {
        assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @DisplayName("삭제")
    @Test
    void delete() {
        assertThat(A1.delete(UserTest.JAVAJIGI))
            .isEqualTo(new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), LocalDateTime.now()));
    }
}
