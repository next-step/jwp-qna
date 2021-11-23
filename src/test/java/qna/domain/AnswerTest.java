package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        Answer actual = answerRepository.save(A1);

        assertThat(actual).isNotNull();
    }

    @Test
    void findById() {
        Answer expected = answerRepository.save(A2);
        Answer actual = answerRepository.findById(A2.getId()).get();

        assertThat(actual).isEqualTo(expected);
    }
}
