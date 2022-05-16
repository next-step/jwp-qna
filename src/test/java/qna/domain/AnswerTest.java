package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save_테스트() {
        Answer managed = answerRepository.save(A1);
        Assertions.assertAll(
                () -> assertThat(managed == A1).isTrue()
        );
    }

    @Test
    void findById_테스트() {
        answerRepository.save(A1);
        Optional<Answer> a1 = answerRepository.findById(A1.getId());
        assertThat(a1.isPresent()).isTrue();
        assertThat(a1.get() == A1).isTrue();
    }

    @Test
    void deleteById_테스트() {
        answerRepository.save(A1);
        answerRepository.deleteById(A1.getId());
        Optional<Answer> a1 = answerRepository.findById(A1.getId());
        assertThat(a1.isPresent()).isFalse();
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        answerRepository.save(A1);
        A1.setDeleted(true);
        answerRepository.save(A2);
        Optional<Answer> a1 = answerRepository.findByIdAndDeletedFalse(A1.getId());
        Optional<Answer> a2 = answerRepository.findByIdAndDeletedFalse(A2.getId());
        assertThat(a1.isPresent()).isFalse();
        assertThat(a2.isPresent()).isTrue();
        assertThat(a2.get() == A2).isTrue();
    }

    @Test
    void findByQuestionIdAndDeletedFalse_테스트() {
        answerRepository.save(A1);
        answerRepository.save(A2);
        questionRepository.save(QuestionTest.Q1);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        assertThat(answerList).containsExactly(A1, A2);
    }
}
