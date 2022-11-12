package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository target;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private TestEntityManager manager;

    private Answer answer;

    @BeforeEach
    public void setUp() {
        final User senior = users.save(
            new User(1L, "senior", "pwd", "name", "gosu@slipp.net"));
        final User newbie = users.save(
            new User(2L, "newbie", "pwd", "name", "new@slipp.net"));

        final Question question = questions.save(
            new Question("JPA", "Proxy")
                .writeBy(senior));

        answer = new Answer(newbie, question, "I'm not sure about that");
    }

    @AfterEach
    void tearDown() {
        manager.flush();
        manager.clear();
    }

    @Test
    void find() {
        final Answer saved = target.save(answer);
        final Answer actual = target.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @Test
    void update() {
        final Question newQuestion = questions.save(new Question("JPA", "MayToOne"));
        final Answer saved = target.save(answer);

        Answer updated = target.findById(saved.getId()).get();
        updated.toQuestion(newQuestion);

        final Answer actual = target.findById(saved.getId()).get();
        assertThat(actual.getQuestion()).isEqualTo(newQuestion);
        assertThat(actual).isSameAs(updated);
    }

    @Test
    void delete() {
        final Answer saved = target.save(answer);

        target.deleteById(saved.getId());
        assertThat(target.findById(saved.getId()).orElse(null)).isNull();
    }

    @Test
    void findByQuestionAndDeletedFalse_nullQuestion() {
        assertThat(target.findByQuestionAndDeletedFalse(null)).isEmpty();
    }

    @Test
    void findByQuestionAndDeletedFalse_notAnsweredYet() {
        final Question newQuestion = questions.save(new Question("JPA", "Entity Mapping Proxy"));
        assertThat(target.findByQuestionAndDeletedFalse(newQuestion)).isEmpty();
    }

    @Test
    void findByQuestionAndDeletedFalse() {
        final Answer saved = target.save(answer);
        assertThat(target.findByQuestionAndDeletedFalse(answer.getQuestion()))
            .containsExactly(saved);
    }

}
