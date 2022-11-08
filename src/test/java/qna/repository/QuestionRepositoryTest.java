package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

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
        Question question = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse()
        );
    }

    @Test
    void save_retreive_test() {
        // given
        Question saveQuestion1 = questionRepository.save(Q1);
        Question saveQuestion2 = questionRepository.save(Q2);
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
        Question saveQuestion = questionRepository.save(Q1);
        Long saveQuestionId = saveQuestion.getId();
        // when
        saveQuestion.setDeleted(true);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);
        // then
        assertThat(findQuestion.isPresent()).isFalse();
    }

    @Test
    void save_delete_test() {
        // given
        Question saveQuestion = questionRepository.save(Q2);
        Long saveQuestionId = saveQuestion.getId();
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);
        // when
        findQuestion.ifPresent(question -> questionRepository.delete(question));
        Optional<Question> deletedQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);
        // then
        assertThat(deletedQuestion.isPresent()).isFalse();
    }
}