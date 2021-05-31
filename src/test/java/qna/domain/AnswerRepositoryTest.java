package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Test
    public void save() {
        User user = new User(1L, "cjs", "password", "name", "chajs226@gmail.com");
        Question question = new Question("initTestTitle", "initTestContents");
        Answer expected = new Answer(user, question, "initTestContents");

        Answer actual = answers.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findByContents() {
        User user = new User(1L, "cjs", "password", "name", "chajs226@gmail.com");
        Question question = new Question("initTestTitle", "initTestContents");
        Answer expected = new Answer(user, question, "initTestContents");

        Answer actual = answers.save(expected);
        assertThat(actual.getContents()).isEqualTo("initTestContents");
    }
}