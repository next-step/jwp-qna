package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Test
    void save() {
        User user = new User("id", "password", "name", "email");
        Question expected = new Question("title", "contents");
        expected.writeBy(user);
        Question actual = questions.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findById() {
        User user = new User("id", "password", "name", "email");
        Question question = new Question("title", "contents");
        question.writeBy(user);
        questions.save(question);
        Optional<Question> actual = questions.findById(0L);
        assertThat(actual).isNotNull();
    }
}
