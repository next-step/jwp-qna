package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Test
    void save_and_find() {
        Question actual = questionRepository.save(Q1);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(Q1.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getWriterId()).isEqualTo(Q1.getWriterId()),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt())
        );
        assertThat(questionRepository.findByIdAndDeletedFalse(actual.getId()).orElseThrow(RuntimeException::new))
            .isEqualTo(actual);
    }

    @Test
    void 복수개_저장_후_리스트_find() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);
        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        assertThat(findQuestions).hasSize(2);
        assertThat(findQuestions).containsExactly(q1, q2);
    }

    @Test
    void find_after_deleted_true() {
        Question savedQuestion = questionRepository.save(Q1);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).isNotEmpty();
        savedQuestion.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).isEmpty();
    }
}