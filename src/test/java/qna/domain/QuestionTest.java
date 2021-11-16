package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents3", true).writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    private void setUp() {
        userRepository.saveAll(Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI));
        questionRepository.saveAll(Arrays.asList(Q1, Q2, Q3));
    }

    private Question createQuestion(String title, String content, User user) {
        final User savedUser = userRepository.save(user);
        return new Question(title, content).writeBy(savedUser);
    }

    @Test
    @DisplayName("Question 을 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        final Question question = createQuestion("title", "content", UserTest.JAVAJIGI);
        final Question savedQuestion = questionRepository.save(question);
        assertEquals(savedQuestion, question);
    }

    @Test
    @DisplayName("삭제되지 않은 Question을 아이디를 통해 찾을 수 있다")
    void findByIdAndDeletedFalse() {
        final Optional<Question> questionOptional = questionRepository.findById(Q1.getId());
        assertAll(() -> {
            assertTrue(questionOptional.isPresent());
            assertEquals(questionOptional.get(), Q1);
        });
    }

    @Test
    @DisplayName("삭제되지 않은 Question를 Question의 아이디를 통해 찾을 수 있다")
    void findByDeletedFalse() {
        final List<Question> foundQuestions = questionRepository.findAll();
        assertAll(() -> {
            assertTrue(foundQuestions.contains(Q1));
            assertTrue(foundQuestions.contains(Q2));
            assertFalse(foundQuestions.contains(Q3));
            assertThat(foundQuestions).containsAll(Arrays.asList(Q1, Q2));
        });
    }

    @Test
    @DisplayName("삭제 된 Question 은 찾을 수 없다.")
    void remove() {
        // given
        final Question question = createQuestion("title", "content", UserTest.JAVAJIGI);
        final Question savedQuestion = questionRepository.saveAndFlush(question);
        // when
        questionRepository.delete(savedQuestion);
        questionRepository.flush();
        // then
        final Optional<Question> deletedQuestion = questionRepository.findById(savedQuestion.getId());
        assertThat(deletedQuestion.isPresent()).isFalse();
    }

    @AfterAll
    void clear() {
        questionRepository.deleteAllInBatch();
    }

}
