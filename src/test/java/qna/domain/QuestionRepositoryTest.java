package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("삭제되지 않은 질문 조회")
    @Test
    void findByDeletedFalse() {
        Question question = questionRepository.save(QuestionTest.Q1);

        List<Question> result = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(question),
            () -> assertThat(result.get(0)).isEqualTo(question)
        );
    }

    @DisplayName("질문 식별자로 삭제되지 않은 질문 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Question question = questionRepository.save(QuestionTest.Q1);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(question)
        );
    }
}