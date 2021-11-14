package qna.domain;

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

    @Test
    void findById() {
        // when
        Question result1 = questionRepository.findById(Q1.getId()).get();
        Question result2 = questionRepository.findById(Q2.getId()).get();

        // then
        assertThat(result1).isEqualTo(Q1);
        assertThat(result2).isEqualTo(Q2);
    }

    @Test
    void update() {
        // given
        Question question = questionRepository.findById(Q1.getId()).get();
        String newContents = "Update Contents";

        // when
        question.setContents(newContents);

        // then
        List<Question> result = questionRepository.findByContents(newContents);
        assertThat(result).containsExactly(Q1);
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
