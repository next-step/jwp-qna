package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private User user;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @DisplayName("question 삭제")
    @Test
    void removeQuestionTest() {
        assertThat(questionRepository.findAll().size()).isEqualTo(1);
        questionRepository.delete(question);
        assertThat(questionRepository.findAll().size()).isZero();
    }

    @DisplayName("삭제되지 않은 question 목록 찾기")
    @Test
    void findByDeletedFalseTest() {
        List<Question> noneDeletedQuestions = questionRepository.findByDeletedFalse();
        assertThat(noneDeletedQuestions.size()).isEqualTo(1);
        Question question = noneDeletedQuestions.get(0);
        assertEquals(question, this.question);
    }

    @DisplayName("삭제한 question 찾기")
    @Test
    void findByIdAndDeletedTrueTest() throws CannotDeleteException {
        question.delete(user);
        assertThatThrownBy(() -> {
            questionRepository.findByIdAndDeletedFalse(question.getId())
                    .orElseThrow(NoSuchElementException::new);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("삭제되지 않은 question 하나 찾기")
    @Test
    void findByIdAndDeletedFalseTest() {
        Question question = questionRepository.findByIdAndDeletedFalse(this.question.getId())
                .orElseThrow(NoSuchElementException::new);
        assertEquals(question, this.question);
    }

    @DisplayName("question delete with user 테스트")
    @Test
    void removeQuestionWithUserTest() throws CannotDeleteException {
        assertThat(questionRepository.findAll().size()).isEqualTo(1);
        question.delete(user);
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isEqualTo(Optional.empty());
    }

    @AfterEach
    void beforeFinish() {
        questionRepository.flush();
    }
}
