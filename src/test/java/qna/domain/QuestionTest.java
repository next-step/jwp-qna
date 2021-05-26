package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @AfterEach
    void deleteAll() {
        questionRepository.deleteAll();
    }

    @Test
    void save() {
        Question q1 = questionRepository.save(Q1);

        questionRepository.findById(q1.getId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Test
    void save2() {
        Question expected = Q2;
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    @Test
    void findById() {
        Question expected = questionRepository.save(Q1);
        Question actual = questionRepository.findById(expected.getId()).get();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete() {
        Question expected = questionRepository.save(Q1);
        questionRepository.delete(expected);

        assertThat(questionRepository.findById(expected.getId())).isNotPresent();
    }

}
