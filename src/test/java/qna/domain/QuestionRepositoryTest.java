package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        Question question = questionRepository.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
                () -> assertThat(question.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }

    @Test
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
    void 질문_삭제_시_deleted_true() {
        Question question = questionRepository.save(QuestionTest.Q1);
        questionRepository.deleteById(question.getId());
        questionRepository.flush();
        Question actual = questionRepository.findById(question.getId()).get();
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    void 삭제되지_않은_질문_조회() {
        Question question = questionRepository.save(QuestionTest.Q1);
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual).isPresent();
    }

    @Test
    void 삭제되지_않은_질문_목록_조회() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).hasSize(2);
    }
}
