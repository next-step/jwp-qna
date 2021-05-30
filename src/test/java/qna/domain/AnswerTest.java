package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private EntityManager entityManager;

    @Test
    void save() {
        Answer expected = AnswerTest.A1;
        Answer actual = answers.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    void findById() {
        Answer expected = answers.save(AnswerTest.A2);
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
        assertThat(actual.get() == expected).isTrue();
    }

    @Test
    void update() {
        Answer expected = answers.save(AnswerTest.A1);
        expected.setDeleted(true);
        entityManager.flush();
        entityManager.clear();
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isDeleted()).isTrue();
    }

    @Test
    void delete() {
        Answer expected = answers.save(AnswerTest.A1);
        answers.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
