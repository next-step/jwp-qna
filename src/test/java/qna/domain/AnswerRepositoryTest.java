package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Test
    void save() {
        User user = new User("id", "password", "name", "email");
        Question question = new Question("title", "contents");
        String contents = "contents";
        Answer expected = new Answer(user, question, contents);
        Answer actual = answers.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findById() {
        User user = new User("id", "password", "name", "email");
        Question question = new Question("title", "contents");
        String contents = "contents";
        answers.save(new Answer(user, question, contents));
        Optional<Answer> actual = answers.findById(0L);
        assertThat(actual).isNotNull();
    }
}
