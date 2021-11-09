package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = new User();
        writer.setId(1L);

        question = new Question();
        question.setId(1L);
    }

    @Test
    void save() {
        Answer expected = new Answer(writer, question, "contents");
        Answer actual = answerRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId())
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer answer1 = new Answer(writer, question, "contents");
        Answer answer2 = new Answer(writer, question, "contents2");
        Answer deletedAnswer = new Answer(writer, question, "contents3");
        deletedAnswer.setDeleted(true);
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        answerRepository.save(deletedAnswer);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(1L);

        assertIterableEquals(answers, Arrays.asList(answer1, answer2));
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer answer1 = new Answer(writer, question, "contents");
        Answer deletedAnswer = new Answer(writer, question, "contents3");
        deletedAnswer.setDeleted(true);
        answerRepository.save(answer1);
        answerRepository.save(deletedAnswer);

        Optional<Answer> actualAnswer1 = answerRepository.findByIdAndDeletedFalse(1L);
        Optional<Answer> actualNull = answerRepository.findByIdAndDeletedFalse(2L);

        assertAll(
            () -> assertThat(actualAnswer1.isPresent()),
            () -> assertThat(actualAnswer1.get()).isEqualTo(answer1),
            () -> assertThat(actualNull.isPresent()).isFalse()
        );
    }
}