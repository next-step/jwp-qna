package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    @Test
    @DisplayName("저장한 Answer 의 id가 null 이 아닌지, 저장 전/후의 데이터 값이 같은지 확인")
    void create() {
        Answer answer = AnswerTest.A1;
        Answer savedAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(answer.getQuestionId())
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 Answer 와 조회한 Answer 가 같은지 (동일성)확인")
    void read() {
        Answer answer = AnswerTest.A1;
        Answer savedAnswer = answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertAll(
                () -> assertThat(findAnswer).isPresent(),
                () -> assertThat(savedAnswer).isSameAs(findAnswer.get())
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 Answer 의 Question 변경 후 조회한 Answer 의 Question 과 같은지 확인")
    void update() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.toQuestion(QuestionTest.Q2);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        Assertions.assertThat(savedAnswer.getQuestionId()).isEqualTo(findAnswer.get().getQuestionId());
    }

    @Transactional
    @Test
    @DisplayName("저장한 Answer 삭제 후 조회 시 Answer 가 없는지 확인")
    void delete() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        answerRepository.delete(savedAnswer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        Assertions.assertThat(findAnswer).isNotPresent();
    }

    @Transactional
    @Test
    @DisplayName("Answer 의 deleted 가 false 일 때 findByIdAndDeletedFalse 로 조회된 Answer 가 있는지 확인")
    void findByIdAndDeletedFalse() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.setDeleted(false);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        Assertions.assertThat(savedAnswer.getId()).isEqualTo(findAnswer.get().getId());
    }

    @Transactional
    @Test
    @DisplayName("Answer 의 deleted 가 true 일 때 findByIdAndDeletedFalse 조회 시 empty 로 조회되는지 확인")
    void findByIdAndDeletedFalse2() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.setDeleted(true);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        Assertions.assertThat(findAnswer).isNotPresent();
    }

    @Transactional
    @Test
    @DisplayName("question id 로 Answer 조회 시 Answer 의 deleted 가 false 인 것만 조회되는지 확인 (모두 false 였을 때)")
    void findByQuestionIdAndDeletedFalse() {
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        savedAnswer1.toQuestion(QuestionTest.Q2);
        savedAnswer1.setDeleted(false);

        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);
        savedAnswer2.toQuestion(QuestionTest.Q2);
        savedAnswer2.setDeleted(false);

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer1.getQuestionId());

        assertAll(
                () -> assertThat(findAnswers).hasSize(2),
                () -> assertThat(findAnswers).containsExactlyInAnyOrder(savedAnswer1, savedAnswer2)
        );
    }

    @Transactional
    @Test
    @DisplayName("question id 로 Answer 조회 시 Answer 의 deleted 가 false 인 것만 조회되는지 확인 (각각 true, false 였을 때)")
    void findByQuestionIdAndDeletedFalse2() {
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        savedAnswer1.toQuestion(QuestionTest.Q2);
        savedAnswer1.setDeleted(true);

        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);
        savedAnswer2.toQuestion(QuestionTest.Q2);
        savedAnswer2.setDeleted(false);

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer1.getQuestionId());

        assertAll(
                () -> assertThat(findAnswers).hasSize(1),
                () -> assertThat(findAnswers).containsExactlyInAnyOrder(savedAnswer2)
        );
    }
}
