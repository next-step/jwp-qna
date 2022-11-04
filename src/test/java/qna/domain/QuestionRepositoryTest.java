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
@DisplayName("질문 테스트")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문 저장 확인")
    void save() {
        Question question = QuestionTest.Q1;
        Question savedQuestion = questionRepository.save(question);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(question.getWriterId())
        );
    }

    @Test
    @DisplayName("저장한 질문과 해당 질문이 같은지 확인")
    void read() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        Optional<Question> question = questionRepository.findById(savedQuestion.getId());

        assertThat(savedQuestion).isEqualTo(question.get());
    }

    @Test
    @DisplayName("저장한 질문 내용 변경 시 내용 일치 여부 확인")
    void update() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        savedQuestion.setContents(QuestionTest.Q2.getContents());

        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());

        assertThat(savedQuestion.getContents()).isEqualTo(findQuestion.get().getContents());
    }

    @Test
    @DisplayName("저장한 질문 삭제 확인")
    void delete() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        questionRepository.delete(savedQuestion);

        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());

        assertThat(findQuestion).isEmpty();
        assertThat(findQuestion).isNotPresent();
    }

    @Test
    @DisplayName("질문 삭제 불가 확인")
    void question_cannot_deleted() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        savedQuestion.setDeleted(false);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(savedQuestion).isEqualTo(findQuestion.get());
    }

    @Test
    @DisplayName("질문 삭제 확인")
    void question_can_deleted() {
        Question saveQuestion = questionRepository.save(QuestionTest.Q1);
        saveQuestion.setDeleted(true);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        assertThat(findQuestion).isEmpty();
    }

    @Test
    @DisplayName("질문 false 개수 확인 1")
    void find_question_not_deleted_count() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        savedQuestion1.setDeleted(false);

        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);
        savedQuestion2.setDeleted(false);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions).contains(savedQuestion1, savedQuestion2);
        assertThat(findQuestions).hasSize(2);

    }

    @Test
    @DisplayName("질문 false 개수 확인 2")
    void find_question_not_deleted_count_2() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        savedQuestion1.setDeleted(false);

        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);
        savedQuestion2.setDeleted(true);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion1);
        assertThat(findQuestions).hasSize(1);
    }

}
