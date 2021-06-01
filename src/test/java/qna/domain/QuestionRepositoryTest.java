package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @BeforeEach
    void setUp() {
        questions.save(QuestionTest.Q1);
    }

    @Test
    public void save() {
        Question expected = QuestionTest.Q2;
        Question actual = questions.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findByTitle() {
        Question actual = questions.findByTitle("title1").get();
        assertThat(actual.getTitle()).isEqualTo("title1");
    }
}