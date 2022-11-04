package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.AnswerTestFixture.A1;
import static qna.domain.AnswerTestFixture.A2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("답변 저장")
    void save_answer() {
        Answer answer = answerRepository.save(A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(answer.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(A1.isDeleted())
        );
    }

    @Test
    @DisplayName("질문 ID로 삭제되지 않은 답변 리스트 조회")
    void find_by_question_id_and_deleted_false() {
        answerRepository.save(A1);
        answerRepository.save(A2);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        assertThat(answers).hasSize(2);
    }

    @Test
    @DisplayName("삭제되지 않은 답변 조회")
    void find_by_id_and_deleted_false() {
        Answer saveAnswer = answerRepository.save(A1);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());
        assertTrue(findAnswer.isPresent());
        findAnswer.ifPresent(answer ->
                assertAll(
                        () -> assertThat(answer.getId()).isNotNull(),
                        () -> assertThat(answer.getId()).isEqualTo(saveAnswer.getId()),
                        () -> assertThat(answer.getQuestionId()).isEqualTo(saveAnswer.getQuestionId()),
                        () -> assertThat(answer.getContents()).isEqualTo(saveAnswer.getContents()),
                        () -> assertThat(answer.getWriterId()).isEqualTo(saveAnswer.getWriterId()),
                        () -> assertThat(answer.getCreatedAt()).isEqualTo(saveAnswer.getCreatedAt()),
                        () -> assertThat(answer.getUpdatedAt()).isEqualTo(saveAnswer.getUpdatedAt())
                )
        );
    }
}
