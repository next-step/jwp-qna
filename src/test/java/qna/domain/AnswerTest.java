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

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        A1.setQuestion(savedQuestion);

        Answer savedAnswer = answerRepository.save(A1);

        assertAll(
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(QuestionTest.Q1.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        A1.setQuestion(savedQuestion);
        Long savedId = answerRepository.save(A1).getId();

        Answer savedAnswer = answerRepository.findByIdAndDeletedFalse(savedId).get();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isEqualTo(savedAnswer.getId()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(QuestionTest.Q1.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
