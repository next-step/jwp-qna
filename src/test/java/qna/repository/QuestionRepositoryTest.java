package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;
    private Question question2;

    @BeforeEach
    void setUp() {
        question = Q1;
        question2 = Q2;
        question.setDeleted(false);
        question2.setDeleted(false);

        question = questionRepository.save(question);
        question2 = questionRepository.save(question2);
    }

    @DisplayName("엔티티를 저장하면 정상적으로 저장되어야 한다")
    @Test
    void save_test() {
        assertAll(
            () -> assertThat(question).isNotNull(),
            () -> assertThat(question2).isNotNull()
        );
    }

    @DisplayName("deleted가 false 값을 조회하면 모두 조회되어야 한다")
    @Test
    void find_test() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).hasSize(2);
    }

    @DisplayName("id 로 deleted 가 false 인 것을 조회하면 정상적으로 조회되어야 한다"
        + "deleted 가 true 라면 조회되지 않아야 한다")
    @Test
    void find_test2() {
        question2.setDeleted(true);
        questionRepository.save(question2);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());
        Optional<Question> result2 = questionRepository.findByIdAndDeletedFalse(question2.getId());

        assertAll(
            () -> assertTrue(result.isPresent()),
            () -> assertFalse(result2.isPresent())
        );
    }

    @DisplayName("엔티티의 필드가 수정되면 수정된 상태로 조회되어야 한다")
    @Test
    void update_test() {
        question2.setDeleted(true);
        questionRepository.save(question2);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());
        Optional<Question> result2 = questionRepository.findByIdAndDeletedFalse(question2.getId());

        assertAll(
            () -> assertTrue(result.isPresent()),
            () -> assertFalse(result2.isPresent())
        );
    }

    @DisplayName("entity 를 삭제하면 정상적으로 삭제되어야 한다")
    @Test
    void delete_test() {
        List<Question> questions = questionRepository.findAll();

        questionRepository.delete(question);
        List<Question> deleted_questions = questionRepository.findAll();

        assertAll(
            () -> assertThat(questions).hasSize(2),
            () -> assertThat(deleted_questions).hasSize(1)
        );
    }
}
