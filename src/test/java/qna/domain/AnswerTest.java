package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(
            UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(
            UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answers;

    @BeforeEach
    void setUp() {
        answers.save(A1);
        answers.save(A2);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        assertAll(
                () -> assertThat(actual.get(0).getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(actual.get(0).isDeleted()).isFalse()
        );

    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(A2.getId());
        assertAll(
                () -> assertThat(actual.get().getId()).isEqualTo(A2.getId()),
                () -> assertThat(actual.get().isDeleted()).isFalse()
        );

    }
}
