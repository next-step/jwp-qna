package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @AfterEach
    void clean() {
        questionRepository.deleteAll();
    }

    @Test
    void findById() {
        // given
        Question question = questionRepository.findAll().get(0);

        // when
        Question result = questionRepository.findById(question.getId()).get();

        // then
        assertThat(result).isEqualTo(question);
    }

    @Test
    void update() {
        // given
        Question question = questionRepository.findAll().get(0);
        String newContents = "Update Contents";

        // when
        question.setContents(newContents);

        // then
        List<Question> result = questionRepository.findByContents(newContents);
        assertThat(result).containsExactly(question);
    }

    @Test
    void remove() {
        // given
        List<Question> prevResult = questionRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        questionRepository.deleteAll();

        // then
        List<Question> result = questionRepository.findAll();
        assertThat(result).isEmpty();
    }
}
