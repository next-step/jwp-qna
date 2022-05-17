package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    private DeleteHistory deleteHistory;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    }

    @Test
    @DisplayName("영속 상태의 동일성 보장 검증")
    void verifyEntityPrimaryCacheSave() {
        DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> verifyEqualDeleteHistoryFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("준영속 상태의 동일성 보장 검증")
    void verifyEntityDatabaseSave() {
        DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);
        entityFlushAndClear();
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> verifyEqualDeleteHistoryFields(actual.get(), expected)
        );
    }

    private void entityFlushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    private void verifyEqualDeleteHistoryFields(DeleteHistory dh1, DeleteHistory dh2) {
        assertAll(
                () -> assertThat(dh1.getId()).isEqualTo(dh2.getId()),
                () -> assertThat(dh1.getContentId()).isEqualTo(dh2.getContentId()),
                () -> assertThat(dh1.getContentType()).isEqualTo(dh2.getContentType()),
                () -> assertThat(dh1.getDeletedById()).isEqualTo(dh2.getDeletedById()),
                () -> assertThat(dh1.getCreateDate()).isEqualTo(dh2.getCreateDate())
        );
    }
}
