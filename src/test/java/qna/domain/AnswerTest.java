package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(1L, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 값_검증() {
        assertAll(
                () -> assertThat(A1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(A2.getWriterId()).isEqualTo(UserTest.SANJIGI.getId()),
                () -> assertThat(A1.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(A2.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(A1.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(A2.getContents()).isEqualTo("Answers Contents2")
        );
    }

    @Test
    void 삭제() {
        A1.setDeleted();
        assertThat(A1.isDeleted()).isTrue();
    }
}
