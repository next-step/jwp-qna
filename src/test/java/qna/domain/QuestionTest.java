package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;

@DataJpaTestIncludeAuditing
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;


    @Test
    void save_테스트() {
        Question managed = questionRepository.save(Q1);
        Assertions.assertAll(
                () -> assertThat(managed.getContents().equals(Q1.getContents())).isTrue()
        );
    }

    @Test
    void findById_테스트() {
        Question savedQ1 = questionRepository.save(Q1);
        Optional<Question> q1 = questionRepository.findById(savedQ1.getId());
        assertThat(q1.isPresent()).isTrue();
        assertThat(q1.get() == savedQ1).isTrue();
    }

    @Test
    void deleteById_테스트() {
        Question savedQ1 = questionRepository.save(Q1);
        questionRepository.deleteById(savedQ1.getId());
        Optional<Question> q1 = questionRepository.findById(savedQ1.getId());
        assertThat(q1.isPresent()).isFalse();
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Question savedQ1 = questionRepository.save(Q1);
        Question savedQ2 = questionRepository.save(Q2);
        savedQ1.setDeleted(true);
        Optional<Question> q1 = questionRepository.findByIdAndDeletedFalse(savedQ1.getId());
        Optional<Question> q2 = questionRepository.findByIdAndDeletedFalse(savedQ2.getId());
        assertThat(q1.isPresent()).isFalse();
        assertThat(q2.isPresent()).isTrue();
        assertThat(q2.get() == savedQ2).isTrue();
    }

    @Test
    void findByDeletedFalse_테스트() {
        Question savedQ1 = questionRepository.save(Q1);
        Question savedQ2 = questionRepository.save(Q2);
        List<Question> questionList = questionRepository.findByDeletedFalse();
        assertThat(questionList).containsExactly(savedQ1, savedQ2);
    }
}
