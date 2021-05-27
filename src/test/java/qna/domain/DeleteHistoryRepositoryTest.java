package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    private DeleteHistory saved;

    @BeforeEach
    void setUp() {
        saved = deleteHistoryRepository.save(deleteHistory);
    }

    @AfterEach
    void cleanUp() {
        deleteHistoryRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE delete_history ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("DeleteHistory 저장 테스트")
    void save() {
        assertThat(saved).isEqualTo(deleteHistory);
    }

    @Test
    @DisplayName("DeleteHistory 제거 테스트")
    void delete() {
        deleteHistoryRepository.delete(saved);

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

        assertThat(deleteHistories.contains(saved)).isFalse();
    }

    @Test
    @DisplayName("DeleteHistory 조회 테스트")
    void find() {
        Optional<DeleteHistory> finded = deleteHistoryRepository.findById(saved.getId());

        assertAll(
                () -> assertThat(finded.isPresent()).isTrue(),
                () -> assertThat(finded.get()).isEqualTo(saved)
        );
    }
}
