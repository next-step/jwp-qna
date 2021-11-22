package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        Question actual = questionRepository.save(Q1);

        assertThat(actual).isNotNull();
    }

    @Test
    void findById() {
        Question expected = questionRepository.save(Q2);
        Question actual = questionRepository.findById(Q2.getId()).get();

        assertThat(expected).isEqualTo(actual);
        assertThat(expected).isSameAs(actual);
    }
}
