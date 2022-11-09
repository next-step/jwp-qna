package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);


    @Autowired
    QuestionRepository questionRepository;
    @Test
    void save() {
        Question question = questionRepository.save(Q1);

        assertAll(
                () -> assertThat(question).isNotNull(),
                () -> assertThat(question.getId()).isNotNull()
        );
    }

    @Test
    void findById() {
        String expcted = "title1";

        questionRepository.save(Q1);

        Question question = questionRepository.findById(1L).get();

        assertAll(
                () -> assertThat(question.getTitle()).isEqualTo(expcted)
        );
    }
}
