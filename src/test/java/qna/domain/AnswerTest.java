package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveAndFind() {
        answerRepository.save(A1);
        answerRepository.save(A2);
        flushAndClear();

        Answer answer1 = answerRepository.findById(1L).get();
        Answer answer2 = answerRepository.findById(2L).get();

        assertAll(
                () -> assertThat(answer1.getId()).isEqualTo(1L),
                () -> assertThat(answer2.getId()).isEqualTo(2L)
        );
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
