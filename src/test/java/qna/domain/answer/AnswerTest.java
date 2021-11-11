package qna.domain.answer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.QuestionTest;
import qna.domain.UserTest;
import qna.domain.user.UserRepository;

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
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    private void setUp() {
        userRepository.saveAll(Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI));
        answerRepository.saveAll(Arrays.asList(A1, A2, A3, A4));
    }

    @Test
    @DisplayName("Answer 를 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        final Answer savedAnswer = answerRepository.save(A3);
        assertEquals(savedAnswer, A3);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 아이디를 통해 찾을 수 있다")
    void findByIdAndDeletedFalse() {
        final Answer savedAnswer = answerRepository.save(A1);
        final Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
        assertAll(() -> {
            assertTrue(answerOptional.isPresent());
            assertEquals(answerOptional.get(), A1);
        });
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 Question 의 아이디를 통해 찾을 수 있다")
    void findByQuestionIdAndDeletedFalse() {
        final List<Answer> foundAnswers = answerRepository.findByQuestion_IdAndDeletedFalse(A1.getQuestionId());

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
