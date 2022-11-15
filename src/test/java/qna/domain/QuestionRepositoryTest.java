package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문_저장")
    void 질문_저장() {
        Question question = questionRepository.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
                () -> assertThat(question.getTitle()).isEqualTo(QuestionTest.Q1.getTitle())
        );
    }

    @Test
    @DisplayName("질문_조회")
    void 질문_조회() {
        Question question = questionRepository.save(QuestionTest.Q1);
        Question actual = questionRepository.findById(question.getId()).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
                () -> assertThat(actual.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
                () -> assertThat(actual.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("질문_삭제")
    void 질문_삭제() {
        Question question = questionRepository.save(QuestionTest.Q1);
        questionRepository.deleteById(question.getId());
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotPresent();
    }

    @Test
    @DisplayName("삭제되지_않은_질문_조회")
    void 삭제되지_않은_질문_조회() {
        Question question = questionRepository.save(QuestionTest.Q1);
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("삭제되지_않은_질문_목록_조회")
    void 삭제되지_않은_질문_목록_조회() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).hasSize(2);
    }
}
