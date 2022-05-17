package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void findByDeletedFalse() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        List<Question> actual = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).containsExactly(q1, q2)
        );
    }
}
