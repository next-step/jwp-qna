package qna.domain;

import org.junit.jupiter.api.AfterEach;
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
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    EntityManager entityManager;

    @AfterEach
    void tearDown() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        entityManager
            .createNativeQuery("ALTER TABLE user ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE question ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE answer ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
    }

    @Test
    @DisplayName("답변을 등록할 수 있다.")
    void create() {
        userRepository.save(UserTest.JAVAJIGI);
        questionRepository.save(QuestionTest.Q1);
        answerRepository.save(AnswerTest.A1);

        Optional<Answer> findAnswer = answerRepository.findById(AnswerTest.A1.getId());

        assertThat(findAnswer.isPresent()).isTrue();
        assertThat(findAnswer.get().getId()).isEqualTo(AnswerTest.A1.getId());
    }

    @Test
    @DisplayName("질문에 등록된 답변을 찾을 수 있다.")
    void findByQuestionIdAndDeletedFalse() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestion().getId());

        assertThat(answers.size()).isEqualTo(2);
        assertThat(answers.get(0).getId()).isEqualTo(AnswerTest.A1.getId());
        assertThat(answers.get(1).getId()).isEqualTo(AnswerTest.A2.getId());
    }

    @Test
    @DisplayName("id로 삭제여부를 알 수 있다.")
    void findByIdAndDeletedFalse() {
        userRepository.save(UserTest.JAVAJIGI);
        questionRepository.save(QuestionTest.Q1);
        answerRepository.save(AnswerTest.A1);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A1.getId());

        assertThat(findAnswer.isPresent()).isTrue();
        assertThat(findAnswer.get().getId()).isEqualTo(AnswerTest.A1.getId());
        assertThat(findAnswer.get().isDeleted()).isFalse();
    }
}