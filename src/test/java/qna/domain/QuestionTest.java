package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @Test
    void findByName() {
        questionRepository.save(Q1);
        final List<Question> actual = questionRepository.findByWriterId(UserTest.JAVAJIGI.getId());

        assertThat(actual.get(0)).isEqualTo(Q1);
    }
}
