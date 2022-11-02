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
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    @Test
    @DisplayName("Question 생성 시 id가 null 이 아닌지, 저장 전/후의 데이터 값이 같은지 확인")
    void create() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId())
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 Question 와 조회한 Question 가 같은지 (동일성)확인")
    void read() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);

        Optional<Question> Question = questionRepository.findById(savedQuestion.getId());

        assertAll(
                () -> assertThat(Question).isPresent(),
                () -> assertThat(savedQuestion).isSameAs(Question.get())
        );
    }

    @Transactional
    @Test
    @DisplayName("Question 의 deleted 가 false 일 때 findByIdAndDeletedFalse 로 조회된 Question 가 있는지 확인")
    void findByIdAndDeletedFalse() {
        Question saveQuestion = questionRepository.save(QuestionTest.Q1);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        Assertions.assertThat(saveQuestion.getId()).isEqualTo(findQuestion.get().getId());
    }

    @Transactional
    @Test
    @DisplayName("Question 의 deleted 가 true 일 때 findByIdAndDeletedFalse 조회 시 empty 로 조회되는지 확인")
    void findByIdAndDeletedFalse2() {
        Question saveQuestion = questionRepository.save(QuestionTest.Q1);
        saveQuestion.deleted();

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        Assertions.assertThat(findQuestion).isNotPresent();
    }

    @Transactional
    @Test
    @DisplayName("id 로 Question 조회 시 Question 의 deleted 가 false 인 것만 조회되는지 확인 (모두 false 였을 때)")
    void findByQuestionIdAndDeletedFalse() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);

        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestions).hasSize(2),
                () -> assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion1, savedQuestion2)
        );
    }

    @Transactional
    @Test
    @DisplayName("question id 로 Question 조회 시 Question 의 deleted 가 false 인 것만 조회되는지 확인 (각각 true, false 였을 때)")
    void findByQuestionIdAndDeletedFalse2() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        savedQuestion1.deleted();

        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestions).hasSize(1),
                () -> assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion2)
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 Question 삭제 후 조회 시 Question 가 없는지 확인")
    void delete() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        questionRepository.delete(savedQuestion);

        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());

        assertThat(findQuestion).isNotPresent();
    }
}
