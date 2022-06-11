package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    Question question1;
    Question question2;
    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        question1 = new Question(1L, "hi", "hello~", new Answers());
        question2 = new Question(2L, "wow", "yeah~", new Answers());
    }

    @AfterEach
    void tearDown() {
        questionRepository.deleteAll();

        entityManager
            .createNativeQuery("ALTER TABLE question ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
    }

    @Test
    @DisplayName("질문을 등록할 수 있다.")
    void save() {
        Question saveQuestion = questionRepository.save(question1);

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());

        assertThat(findQuestion.isPresent()).isTrue();
        assertThat(findQuestion.get().getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("deleted 가 false 인 질문을 찾는다.")
    void findByDeletedFalse() {
        questionRepository.save(question1);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions.size()).isEqualTo(1);
        assertThat(findQuestions.get(0).getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문을 찾는다.")
    void findByIdAndDeletedFalse() {
        questionRepository.save(question1);

        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(question1.getId());

        assertThat(question.isPresent()).isTrue();
        assertThat(question.get().getId()).isEqualTo(question1.getId());
    }
}