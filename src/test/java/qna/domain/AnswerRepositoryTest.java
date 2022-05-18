package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.domain.AnswerTest.A1;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("영속 상태의 동일성 보장 검증")
    void verifyEntityPrimaryCacheSave() {
        Answer expected = answerRepository.save(A1);
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(expected),
                () -> verifyEqualAnswerFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("준영속 상태의 동일성 보장 검증")
    void verifyEntityDatabaseSave() {
        Answer expected = answerRepository.save(A1);
        entityFlushAndClear();
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> verifyEqualAnswerFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("엔티티 컨텐츠가 반영되는지 검증")
    void verifyUpdateEntity() {
        Answer expected = answerRepository.save(A1);
        expected.setContents("mond");
        entityFlushAndClear();
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> verifyEqualAnswerFields(actual.get(), expected),
                () -> assertEquals("mond", actual.get().getContents())
        );
    }

    @Test
    @DisplayName("저장 및 물리 삭제 후 해당 id로 검색")
    void saveAndPhysicalDeleteThenFindById() {
        Answer expected = answerRepository.save(A1);
        answerRepository.delete(expected);
        entityFlushAndClear();
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertThat(actual).isNotPresent();
    }
    
    @Test
    @DisplayName("저장 및 논리 삭제 후 해당 id로 검색")
    void sandAndLogicalDeleteThenFindById() {
        Answer expected = answerRepository.save(A1);
        expected.setDeleted(true);
        entityFlushAndClear();
        Optional<Answer> actualOfFindById = answerRepository.findById(expected.getId());
        Optional<Answer> actualOfFindByIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(expected.getId());
        
        assertAll(
                () -> assertThat(actualOfFindById).isPresent(),
                () -> assertThat(actualOfFindByIdAndDeletedFalse).isNotPresent()
        );
    }
    
    private void entityFlushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    private void verifyEqualAnswerFields(Answer a1, Answer a2) {
        assertAll(
                () -> assertThat(a1.getId()).isEqualTo(a2.getId()),
                () -> assertThat(a1.getWriter()).isEqualTo(a2.getWriter()),
                () -> assertThat(a1.getContents()).isEqualTo(a2.getContents()),
                () -> assertThat(a1.getCreatedAt()).isEqualTo(a2.getCreatedAt()),
                () -> assertThat(a1.getUpdatedAt()).isEqualTo(a2.getUpdatedAt())
        );
    }
}
