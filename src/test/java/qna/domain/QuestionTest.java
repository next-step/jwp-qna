package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save_테스트() {
        Question managed = questionRepository.save(Q1);
        Assertions.assertAll(
                () -> assertThat(managed == Q1).isTrue()
        );
    }

    @Test
    void findById_테스트() {
        questionRepository.save(Q1);
        Optional<Question> q1 = questionRepository.findById(Q1.getId());
        assertThat(q1.isPresent()).isTrue();
        assertThat(q1.get() == Q1).isTrue();
    }

    @Test
    void deleteById_테스트() {
        questionRepository.save(Q1);
        questionRepository.deleteById(Q1.getId());
        Optional<Question> q1 = questionRepository.findById(Q1.getId());
        assertThat(q1.isPresent()).isFalse();
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        questionRepository.save(Q1);
        Q1.setDeleted(true);
        questionRepository.save(Q2);
        Optional<Question> q1 = questionRepository.findByIdAndDeletedFalse(Q1.getId());
        Optional<Question> q2 = questionRepository.findByIdAndDeletedFalse(Q2.getId());
        assertThat(q1.isPresent()).isFalse();
        assertThat(q2.isPresent()).isTrue();
        assertThat(q2.get() == Q2).isTrue();
    }

    @Test
    void findByQuestionIdAndDeletedFalse_테스트() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        questionRepository.findByDeletedFalse();
        List<Question> questionList = questionRepository.findByDeletedFalse();
        assertThat(questionList).containsExactly(Q1, Q2);
    }
}
