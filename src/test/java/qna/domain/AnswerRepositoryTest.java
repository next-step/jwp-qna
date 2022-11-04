package qna.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("데이터 저장 확인")
    void save() {
        Answer answer = AnswerTest.A1;
        Answer savedAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents())
        );
    }

    @Test
    @DisplayName("저장한 답변과 해당 답변이 같은지 확인")
    void read() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(savedAnswer).isEqualTo(findAnswer.get());
    }

    @Transactional
    @DisplayName("저장한 답변의 질문 변경 시 일치 여부 확인")
    void update() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.toQuestion(QuestionTest.Q2);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(savedAnswer.getQuestionId()).isEqualTo(findAnswer.get().getQuestionId());
    }

    @Test
    @DisplayName("저장한 답변 삭제 확인")
    void delete() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        answerRepository.delete(savedAnswer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(findAnswer).isEmpty();
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("답변 삭제 불가 확인")
    void answer_cannot_deleted() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(savedAnswer).isEqualTo(findAnswer.get());
    }

    @Test
    @DisplayName("답변 삭제 확인")
    void answer_can_deleted() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.setDeleted(true);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(findAnswer).isEmpty();
    }

    @Test
    @DisplayName("답변 false 개수 확인 1")
    void find_answer_not_deleted_count() {
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        savedAnswer1.setDeleted(false);

        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);
        savedAnswer2.setDeleted(false);

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer2.getQuestionId());

        assertThat(findAnswers).contains(savedAnswer1, savedAnswer2);
        assertThat(findAnswers).hasSize(2);

    }

    @Test
    @DisplayName("답변 false 개수 확인 2")
    void find_answer_not_deleted_count_2() {
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        savedAnswer1.toQuestion(QuestionTest.Q2);
        savedAnswer1.setDeleted(false);

        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);
        savedAnswer1.toQuestion(QuestionTest.Q2);
        savedAnswer2.setDeleted(true);

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer2.getQuestionId());

        assertThat(findAnswers).containsExactlyInAnyOrder(savedAnswer1);
        assertThat(findAnswers).hasSize(1);
    }

}
