package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

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
        // given
        final Answer answer = answerRepository.save(A1);

        // when
        final Long actual = answer.getId();

        // then
        assertThat(actual).isEqualTo(A1.getId());
    }

    @Test
    void findById() {
        // given
        final Answer expected = answerRepository.save(A1);

        // when
        final Optional<Answer> optAnswer = answerRepository.findById(UserTest.JAVAJIGI.getId());
        final Answer actual = optAnswer.orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
