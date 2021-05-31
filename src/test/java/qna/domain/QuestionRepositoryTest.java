package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Test
    public void save() {
        Question expected = new Question("titleTest", "contentsTest");

        Question actual = questions.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findByTitle() {
        Question expected = new Question("titleTest", "contentsTest");

        Question actual = questions.save(expected);
        assertThat(actual.getTitle()).isEqualTo("titleTest");
    }
}