package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.TestQuestionFactory;
import qna.domain.TestUserFactory;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAllInBatch();
    }

    @Test
    void save() {
        User writer = TestUserFactory.create("pepe");
        Question question = TestQuestionFactory.create(writer);
        Question saveQuestion = questionRepository.save(question);
        assertAll(
                () -> assertThat(saveQuestion.getId()).isNotNull(),
                () -> assertThat(saveQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(saveQuestion.isDeleted()).isFalse()
        );
    }

    @Test
    void save_retreive_test() {
        // given
        User writer = TestUserFactory.create("pepe");
        Question question = TestQuestionFactory.create(writer);
        User writer2 = TestUserFactory.create("modric");
        Question question2 = TestQuestionFactory.create(writer2);
        Question saveQuestion1 = questionRepository.save(question);
        Question saveQuestion2 = questionRepository.save(question2);
        // when
        List<Question> findQuestion = questionRepository.findByDeletedFalse();
        // then
        assertAll(
                () -> assertThat(findQuestion.size()).isEqualTo(2),
                () -> assertThat(findQuestion).contains(saveQuestion1),
                () -> assertThat(findQuestion).contains(saveQuestion2)
        );
    }

    @Test
    void save_setDelete_true_retreive_test() {
        // given
        User writer = TestUserFactory.create("pepe");
        Question question = TestQuestionFactory.create(writer);
        Question saveQuestion = questionRepository.save(question);
        Long saveQuestionId = saveQuestion.getId();
        // when
        saveQuestion.setDeleted(true);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);
        // then
        assertThat(findQuestion).isNotPresent();
    }

    @Test
    void save_delete_test() {
        // given
        User writer = TestUserFactory.create("pepe");
        Question question = TestQuestionFactory.create(writer);
        Question saveQuestion = questionRepository.save(question);
        Long saveQuestionId = saveQuestion.getId();
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);
        // when
        findQuestion.ifPresent(paramQuestion -> questionRepository.delete(paramQuestion));
        Optional<Question> deletedQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);
        // then
        assertThat(deletedQuestion).isNotPresent();
    }
}
