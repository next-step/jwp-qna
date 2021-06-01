package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Test
    void save() {
        Question expected = QuestionTest.Q1;
        Question actual = questions.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isNotNull(),
                () -> assertThat(actual.isDeleted()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    void findByName() {
        Question expected = QuestionTest.Q2;
        questions.save(expected);
        Question actual = questions.findById(expected.getId()).get();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void update() {
        Question expected = QuestionTest.Q1;
        Question saved = questions.save(expected);

        saved.setContents("Question Contents Changed");
        questions.flush();
    }

    @Test
    void delete() {
        Question expected = QuestionTest.Q1;
        Question saved = questions.save(expected);

        questions.delete(saved);
        questions.flush();
    }


}