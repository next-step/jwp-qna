package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Question question;
    private Answer answer;

    @AfterEach
    public void deleteAll() {
        answerRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        answer = answerRepository.save(new Answer(2L, user, question, "Answers Contents1"));
    }

    @Test
    void findByIdAndDeletedFalse_Test() {
        Answer actual = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        assertThat(answer).isEqualTo(actual);
    }

    @Test
    void findByQuestionIdAndDeletedFalse_Test() {
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(actualList).contains(answer);
    }

    @Test
    void save() {
        Answer expected = new Answer(user, question, "contents");
        Answer actual = answerRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getQuestion()).isEqualTo(expected.getQuestion())
        );
    }

    @Test
    void findById() {
        Answer actual = answerRepository.findById(answer.getId()).get();

        assertThat(actual).isEqualTo(answer);
    }

    @Test
    void delete() {
        answerRepository.delete(answer);

        assertThat(answerRepository.findById(answer.getId())).isNotPresent();
    }

}
