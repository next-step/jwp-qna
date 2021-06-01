package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Test
    public void save() {
        //User user = new User(1L, "cjs", "password", "name", "chajs226@gmail.com");
        //Question question = new Question("initTestTitle", "initTestContents");
        //Answer answer = new Answer(user, question, "initTestContents");

        User user = users.save(JAVAJIGI);
        Question question = questions.save(Q1);
        A1.toQuestion(question);
        Answer answer = answers.save(A1);

        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), user, LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(expected);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findByContentId() {
        User user = new User(1L, "cjs", "password", "name", "chajs226@gmail.com");
        Question question = new Question("initTestTitle", "initTestContents");
        Answer answer = new Answer(user, question, "initTestContents");
        Answer expectedAnswer = answers.save(answer);

        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), user, LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(expected);

        assertThat(actual.getContentId()).isEqualTo(expectedAnswer.getId());
    }
}