package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변_저장")
    void 답변_저장() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId()),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents()),
                () -> assertThat(answer.isDeleted()).isFalse(),
                () -> assertThat(answer.getCreatedAt()).isNotNull(),
                () -> assertThat(answer.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("답변_저장_후_조회")
    void 답변_저장_후_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Answer actual = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(AnswerTest.A1.getContents()),
                () -> assertThat(actual.isDeleted()).isFalse(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("답변_삭제")
    void 답변_삭제() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answerRepository.deleteById(answer.getId());
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isNotPresent();
    }

    @Test
    @DisplayName("ID로_질문_조회")
    void ID로_질문_조회() {
        answerRepository.save(AnswerTest.A1);
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("삭제되지_않은_답변_조회")
    void 삭제되지_않은_답변_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual).isPresent();
    }
}
