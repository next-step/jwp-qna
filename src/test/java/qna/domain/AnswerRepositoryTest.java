package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    public static final Answer A1 = new Answer(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void create() {
        Answer answer = answerRepository.save(A1);
        assertThat(answer.getId()).isEqualTo(A1.getId());
    }

    @Test
    void findById() {
        Answer answer = answerRepository.save(A1);
        Optional<Answer> expected = answerRepository.findById(answer.getId());
        assertThat(expected.isPresent()).isTrue();
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.save(A1);
        Optional<Answer> expected = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(expected.isPresent()).isTrue();
    }

    @Test
    void findByQuestionId() {
        Answer answer = answerRepository.save(A1);
        assertThat(answer.getQuestionId()).isEqualTo(A1.getQuestionId());
    }
}
