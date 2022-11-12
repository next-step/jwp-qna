package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 질문_저장() {
        Question actual = new Question(1L, "title", "contents");
        Question expected = questionRepository.save(actual);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void id로_질문조회() {
        Question actual = new Question(1L, "title", "contents");
        questionRepository.save(actual);
        Optional<Question> expected = questionRepository.findById(1L);
        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(actual);
    }

    @Test
    void findByDeletedFalse() {
        Question actual = new Question(1L, "title", "contents");
        questionRepository.save(actual);
        List<Question> expected = questionRepository.findAllByDeletedFalse();
        assertThat(expected).hasSize(1);
        assertThat(expected).contains(actual);
    }
}
