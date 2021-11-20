package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private EntityManager entityManager;

    @Test
    void instantiate() {
        assertThat(A1.getId()).isNull();
        assertThat(A1.getWriter()).isEqualTo(UserTest.JAVAJIGI);
        assertThat(A1.getQuestion()).isEqualTo(QuestionTest.Q1);
        assertThat(A1.getContents()).isEqualTo("Answers Contents1");
        assertThat(A1.isDeleted()).isFalse();
        assertThat(A1.getCreatedAt()).isNull();
        assertThat(A1.getUpdatedAt()).isNull();
    }

    @Test
    void save() {
        final Question question = questions.save(QuestionTest.Q1);
        final User user = users.save(UserTest.JAVAJIGI);
        LocalDateTime now = LocalDateTime.now();

        final Answer actual = answers.save(
                new Answer(user, question, "Answers Contents1")
        );

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getWriter()).isEqualTo(user);
        assertThat(actual.getQuestion()).isEqualTo(question);
        assertThat(actual.getContents()).isEqualTo("Answers Contents1");
        assertThat(actual.isDeleted()).isEqualTo(false);
        assertThat(actual.getCreatedAt()).isAfterOrEqualTo(now);
        assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    void findByName() {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1);

        final Answer expected = answers.save(
                new Answer(user, question, "Answers Contents1")
        );
        final Answer actual = answers.findById(expected.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getWriter()).isEqualTo(expected.getWriter());
    }

    @Test
    void update() {
        final LocalDateTime now = LocalDateTime.now();
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1);
        final Answer answer = answers.save(
                new Answer(user, question, "Answers Contents1")
        );
        String content = "Updated Content";

        answer.setContents(content);

        final Answer actual = answers.findById(answer.getId()).get();
        assertThat(actual.getContents()).isEqualTo(content);
        assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    void updateQuestion() {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question1 = questions.save(QuestionTest.Q1);
        final Question question2 = questions.save(QuestionTest.Q2);
        final Answer answer = answers.save(
                new Answer(user, question1, "Answers Contents1")
        );

        answer.setQuestion(question2);
        entityManager.flush();

        assertThat(answer.getQuestion()).isEqualTo(question2);
    }

    @Test
    void delete() {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1);

        final Answer answer = answers.save(
                new Answer(user, question, "Answers Contents1")
        );
        assertThat(answers.findById(answer.getId())).isNotNull();
        answers.deleteById(answer.getId());
        assertThat(answers.findById(answer.getId())).isEmpty();
    }
}
