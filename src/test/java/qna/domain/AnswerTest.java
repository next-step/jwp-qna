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
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Answer answer;
    private Question question;
    private User user;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
    }

    @DisplayName("answer 생성")
    @Test
    void saveAnswerTest () {
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(answer.getWriter()).isEqualTo(user),
                () -> assertThat(answer.getQuestion()).isEqualTo(question)
        );
    }

    @DisplayName("answer 수정")
    @Test
    void updateAnswerTest() {
        answer.changeContents("Changed Contents1");
        Answer actual = answerRepository.findById(answer.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(actual.getContents()).isEqualTo("Changed Contents1");
    }

    @DisplayName("answer save with question 추가")
    @Test
    void saveQuestionWithAnswerTest() {
        answer.changeQuestion(question);
        Question actual = questionRepository.findById(question.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(actual.getContents()).isEqualTo("contents1"),
                () -> assertThat(actual.getAnswers().size()).isEqualTo(1),
                () -> assertThat(actual.getAnswers().get(0)).isEqualTo(answer)
        );
    }

    @DisplayName("answer remove with question 삭제")
    @Test
    void removeQuestionWithAnswerTest() {
        answer.removeQuestion();
        Question actual = questionRepository.findById(question.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(actual.getContents()).isEqualTo("contents1"),
                () -> assertThat(actual.getAnswers().size()).isZero()
        );
    }

    @DisplayName("validate owner exception")
    @Test
    void removeAnswerWithOtherUserExceptionTest() {
        assertThatThrownBy(() -> {
            final User otherUser = userRepository.save(UserTest.LEWISSEO);
            answer.delete(otherUser);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @AfterEach
    void beforeFinish() {
        answerRepository.flush();
        questionRepository.flush();
        userRepository.flush();
    }
}
