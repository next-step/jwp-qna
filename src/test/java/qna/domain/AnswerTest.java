package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        Question question = questionRepository.save(new Question("questionTitle", "questionContents"));
        User user = userRepository.save(UserTest.JAVAJIGI);
        Answer actual = answerRepository.save(new Answer(user, question, "Answer Contents"));

        assertThat(actual).isNotNull();
    }

    @Test
    void findById() {
        Question question = questionRepository.save(new Question("questionTitle", "questionContents"));
        User user = userRepository.save(UserTest.SANJIGI);
        Answer expected = answerRepository.save(new Answer(user, question, "Answer Contents"));
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertThat(actual).hasValue(expected);
    }

    @Test
    void createWithWriterAndQuestion() {
        Question question = questionRepository.save(new Question("questionTitle", "questionContents"));
        User user = userRepository.save(UserTest.JAVAJIGI);
        Answer actual = answerRepository.save(new Answer(user, question, "answer Contents"));

        assertThat(actual).isNotNull();
        assertThat(actual.getQuestion()).isEqualTo(question);
        assertThat(actual.getWriter()).isEqualTo(user);
    }

    @Test
    void readWithWriterAndQuestion() {
        Question question = questionRepository.save(new Question("questionTitle", "questionContents"));
        User user = userRepository.save(UserTest.JAVAJIGI);
        Answer expected = answerRepository.save(new Answer(user, question, "answer Contents"));

        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertThat(actual).hasValue(expected);
    }
}
