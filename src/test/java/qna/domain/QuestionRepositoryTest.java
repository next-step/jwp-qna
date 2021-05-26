package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    private Question question = QuestionTest.Q1;
    private Question newQuestion = QuestionTest.Q2;
    private Question saved;

    @BeforeEach
    void setUp() {
        saved = questionRepository.save(question);
    }

    @AfterEach
    void cleanUp() {
        question.setDeleted(false);
        newQuestion.setDeleted(false);
        questionRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE question ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("Question 저장 테스트")
    void save() {
        assertThat(saved).isEqualTo(question);
    }

    @Test
    @DisplayName("Question 수정 테스트")
    void update() {
        String changedContents = "질문 내용 바꾸기";
        saved.setContents(changedContents);

        Optional<Question> updated = questionRepository.findById(saved.getId());

        assertThat(updated.get().getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("Question 제거 테스트")
    void delete() {
        questionRepository.delete(saved);

        List<Question> questions = questionRepository.findAll();

        assertThat(questions.contains(saved)).isFalse();
    }

    @Test
    @DisplayName("전체 question 조회 테스트")
    void findByDeletedFalse() {
        questionRepository.save(newQuestion);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(2);
        assertThat(questions).contains(saved, newQuestion);
    }

    @Test
    @DisplayName("전체 questions에서 question이 삭제된 경우 조회 실패 테스트")
    void findByDeletedFalse_failCase() {
        questionRepository.save(newQuestion);

        newQuestion.setDeleted(true);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(1);
        assertThat(questions).doesNotContain(newQuestion);
    }

    @Test
    @DisplayName("question 아이디 값으로 조회 테스트")
    void findByIdAndDeletedFalse() {
        Optional<Question> finded = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertAll(
                () -> assertThat(finded.isPresent()).isTrue(),
                () -> assertThat(finded.get()).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("question 아이디 값으로 조회시 삭제되었으면 조회 실패 테스트")
    void findByIdAndDeletedFalse_failCase() {
        question.setDeleted(true);

        Optional<Question> finded = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertThat(finded.isPresent()).isFalse();
    }
}
