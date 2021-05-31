package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private AnswerRepository answers;

    @Test
    public void save() {
        User user = new User(1L, "cjs", "password", "name", "chajs226@gmail.com");
        Question question = new Question("initTestTitle", "initTestContents");
        Answer answer = new Answer(user, question, "initTestContents");
        answers.save(answer);

        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), user.getId(), LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(expected);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findByContentId() {
        User user = new User(1L, "cjs", "password", "name", "chajs226@gmail.com");
        Question question = new Question("initTestTitle", "initTestContents");
        Answer answer = new Answer(user, question, "initTestContents");
        Answer expectedAnswer = answers.save(answer);

        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), user.getId(), LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(expected);

        assertThat(actual.getContentId()).isEqualTo(expectedAnswer.getId());
    }
}