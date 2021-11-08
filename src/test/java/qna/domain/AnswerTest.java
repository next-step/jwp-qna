package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private static Answer saved;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        saved = answerRepository.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "너무 어렵게 생각하지 마라."));
    }

    @Test
    void save() {
        Answer actual = answerRepository.save(A2);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getContents()).isEqualTo(A2.getContents());
    }

    @Test
    void identity() {
        Answer actual = answerRepository.findById(saved.getId()).get();
        assertThat(actual == saved).isTrue();
    }

    @Test
    void update() {
        Answer expected = answerRepository.findById(saved.getId()).get();
        expected.setContents("하고 싶은대로 하면서 살면 된다.");

        Answer actual = answerRepository.findById(saved.getId()).get();
        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    void delete() {
        answerRepository.deleteById(saved.getId());
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> answerRepository.findById(saved.getId()).get()
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(saved.getQuestionId());
        assertThat(answers).isNotEmpty();
    }

    @Test
    void findByIdAndDeletedFalse() {
        saved.setDeleted(true);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> answerRepository.findByIdAndDeletedFalse(saved.getId()).get()
        );
    }
}
