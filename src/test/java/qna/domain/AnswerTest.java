package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        Answer savedAnswer = answerRepository.save(A1);

        assertAll(
            () -> assertThat(savedAnswer.getId()).isEqualTo(savedAnswer.getId()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(A1.getQuestionId()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        Long savedId = answerRepository.save(A1).getId();
        Answer savedAnswer = answerRepository.findByIdAndDeletedFalse(savedId).get();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isEqualTo(savedAnswer.getId()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(A1.getQuestionId()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
