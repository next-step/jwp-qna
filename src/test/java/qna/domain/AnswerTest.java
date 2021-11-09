package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {

    static {
        QuestionTest.Q1.setId(1L);
        UserTest.JAVAJIGI.setId(1L);
        UserTest.SANJIGI.setId(2L);
    }

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        saveAndIdUpdate(A1);
        saveAndIdUpdate(A2);
    }

    private void saveAndIdUpdate(final Answer answer) {
        final Answer savedAnswer = answerRepository.save(answer);
        answer.setId(savedAnswer.getId());
    }

    @Test
    void Answer_를_저장_할_경우_저장된_객체와_저장_후_객체가_일치하다() {
        final Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents3");
        final Answer savedAnswer = answerRepository.save(answer);
        assertEquals(savedAnswer, answer);
    }

    @Test
    void 삭제되지_않은_Answer를_아이디를_통해_찾을_수_있다() {
        final Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(A1.getId());
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
            assertThat(foundAnswers).containsAll(Arrays.asList(A1, A2));
        });
    }


}
