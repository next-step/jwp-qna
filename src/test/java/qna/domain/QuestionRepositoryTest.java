package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("준영속 상태의 동일성 보장 검증")
    void verifyEntityPrimaryCacheSave() {
        Question expected = questionRepository.save(Q1);
        Optional<Question> actual = questionRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(expected),
                () -> verifyEqualQuestionFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("영속 상태의 동일성 보장 검증")
    void verifyEntityDatabaseSave() {
        Question expected = questionRepository.save(Q1);
        entityFlushAndClear();
        Optional<Question> actual = questionRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(expected),
                () -> verifyEqualQuestionFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("저장 및 물리 삭제 후 해당 id로 검색")
    void saveAndPhysicalDeleteThenFindById() {
        Question expected = questionRepository.save(Q1);
        questionRepository.delete(expected);
        entityFlushAndClear();
        Optional<Question> actual = questionRepository.findById(expected.getId());

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("저장 및 논리 삭제 후 해당 id로 검색")
    void sandAndLogicalDeleteThenFindById() {
        Question expected = questionRepository.save(Q1);
        expected.setDeleted(true);
        entityFlushAndClear();
        Optional<Question> actualOfFindById = questionRepository.findById(expected.getId());
        Optional<Question> actualOfFindByIdAndDeletedFalse = questionRepository.findByIdAndDeletedFalse(
                expected.getId());

        assertAll(
                () -> assertThat(actualOfFindById).isPresent(),
                () -> assertThat(actualOfFindByIdAndDeletedFalse).isNotPresent()
        );
    }

    private void entityFlushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    private void verifyEqualQuestionFields(Question q1, Question q2) {
        assertAll(
                () -> assertThat(q1.getId()).isEqualTo(q2.getId()),
                () -> assertThat(q1.getTitle()).isEqualTo(q2.getTitle()),
                () -> assertThat(q1.getContents()).isEqualTo(q2.getContents()),
                () -> assertThat(q1.getCreatedAt()).isEqualTo(q2.getCreatedAt()),
                () -> assertThat(q1.getUpdatedAt()).isEqualTo(q2.getUpdatedAt())
        );
    }
}
