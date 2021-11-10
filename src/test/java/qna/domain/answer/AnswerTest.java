package qna.domain.answer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnswerTest {

    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    public static final Answer A3 = new Answer(3L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents3", true);
    public static final Answer A4 = new Answer(4L, UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents4");

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeAll
    private void setUp() {
        answerRepository.saveAll(Arrays.asList(A1, A2, A3, A4));
    }

    @Test
    void Answer_를_저장_할_경우_저장된_객체와_저장_후_객체가_일치하다() {
        final Answer savedAnswer = answerRepository.save(A3);
        assertEquals(savedAnswer, A3);
    }

    @Test
    void 삭제되지_않은_Answer를_아이디를_통해_찾을_수_있다() {
        final Answer savedAnswer = answerRepository.save(A1);
        final Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
        assertAll(() -> {
            assertTrue(answerOptional.isPresent());
            assertEquals(answerOptional.get(), A1);
        });
    }

    @Test
    void 삭제되지_않은_Answer를_Question의_아이디를_통해_찾을_수_있다() {

        final List<Answer> foundAnswers = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());

        assertAll(() -> {
            assertTrue(foundAnswers.contains(A1));
            assertTrue(foundAnswers.contains(A2));
            assertFalse(foundAnswers.contains(A3));
            assertFalse(foundAnswers.contains(A4));
            assertThat(foundAnswers).containsAll(Arrays.asList(A1, A2));
        });
    }

    @AfterAll
    void clear() {
        answerRepository.deleteAllInBatch();
    }


}
