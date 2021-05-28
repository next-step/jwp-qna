package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writtenBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writtenBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writtenBy(user));
    }

    @AfterEach
    void deleteAll() {
        questionRepository.deleteAll();
    }

    @Test
    void save() {
        Question expected = new Question("title1", "contents1").writtenBy(user);
        Question actual = questionRepository.save(expected);

        questionRepository.findById(actual.getId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Test
    void save2() {
        Question expected = new Question("title1", "contents1").writtenBy(user);
        Question actual = questionRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    @Test
    void findById() {
        Question actual = questionRepository.findById(question.getId()).get();

        assertThat(actual).isEqualTo(question);
    }

    @Test
    void update() {
        assertThat(question.getWriter()).isEqualTo(user);

        User expected = userRepository.save(new User("userId", "password", "name", "email"));
        question.setWriter(expected);

        assertThat(questionRepository.findById(question.getId()).get().getWriter()).isEqualTo(expected);
    }

    @Test
    void delete() {
        questionRepository.delete(question);

        assertThat(questionRepository.findById(question.getId())).isNotPresent();
    }

}
