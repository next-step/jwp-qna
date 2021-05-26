package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    private Question question = QuestionTest.Q1;
    private Question saved;

    @BeforeEach
    void setUp() {
        saved = questionRepository.save(question);
    }

    @AfterEach
    void cleanUp() {
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
        saved.setContents("질문 내용 바꾸기");

        Optional<Question> updated = questionRepository.findById(saved.getId());

        assertThat(updated.get().getContents()).isEqualTo("질문 내용 바꾸기");
    }

    @Test
    @DisplayName("Question 제거 테스트")
    void delete() {
        questionRepository.delete(saved);

        List<Question> questions = questionRepository.findAll();

        assertThat(questions).isEmpty();
    }

    @Test
    void findByDeletedFalse() {
    }

    @Test
    void findByIdAndDeletedFalse() {
    }
}