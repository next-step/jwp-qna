package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTest {

    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question(3L, "title3", "contents3", true).writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    private void setUp() {
        questionRepository.saveAll(Arrays.asList(Q1, Q2, Q3));
    }

    private Question createQuestion(String title, String content, User user) {
        return new Question(title, content).writeBy(user);
    }

    @Test
    void Question_을_저장_할_경우_저장된_객체와_저장_후_객체가_일치하다() {
        final Question question = createQuestion("title", "content", UserTest.JAVAJIGI);
        final Question savedQuestion = questionRepository.save(question);
        assertEquals(savedQuestion, question);
    }

    @Test
    void 삭제되지_않은_Question을_아이디를_통해_찾을_수_있다() {
        final Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(Q1.getId());
        assertAll(() -> {
            assertTrue(questionOptional.isPresent());
            assertEquals(questionOptional.get(), Q1);
        });
    }

    @Test
    void 삭제되지_않은_Question를_Question의_아이디를_통해_찾을_수_있다() {
        final List<Question> foundQuestions = questionRepository.findByDeletedFalse();
        assertAll(() -> {
            assertTrue(foundQuestions.contains(Q1));
            assertTrue(foundQuestions.contains(Q2));
            assertFalse(foundQuestions.contains(Q3));
            assertThat(foundQuestions).containsAll(Arrays.asList(Q1, Q2));
        });
    }

    @AfterAll
    void clear() {
        questionRepository.deleteAllInBatch();
    }

}
