package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        Question actual = questionRepository.save(Q1);

        assertThat(actual).isNotNull();
    }

    @Test
    void findById() {
        Question expected = questionRepository.save(Q2);
        Optional<Question> actual = questionRepository.findById(expected.getId());

        assertThat(actual).hasValue(expected);
    }

    @Test
    void createWithWriter() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = new Question("questionTitle", "questionContents").writeBy(user);
        Question actual = questionRepository.save(question);

        assertThat(actual).isNotNull();
        assertThat(actual.getWriter()).isNotNull();
    }

    @Test
    void readWithWriter() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = new Question("questionTitle", "questionContents").writeBy(user);

        Optional<Question> actual = questionRepository.findById(questionRepository.save(question).getId());
        assertThat(actual).hasValue(question);
    }

    @Test
    void updateWriter() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        User user2 = userRepository.save(UserTest.SANJIGI);
        Question actual = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));

        actual.writeBy(user2);

        assertThat(actual.getWriter()).isEqualTo(user2);
    }

    @Test
    void deleteWriter() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question actual = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));

        actual.writeBy(null);

        assertThat(actual.getWriter()).isNull();
    }
}
