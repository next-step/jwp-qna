package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    private DeleteHistory deleteHistory;
    private DeleteHistory deleteHistory2;

    @BeforeEach
    void setUp() {
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L,
            LocalDateTime.now());
        deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 1L, 1L,
            LocalDateTime.now());

        deleteHistoryRepository.save(deleteHistory);
        deleteHistoryRepository.save(deleteHistory2);
    }

    @DisplayName("DeleteHistory 를 저장하면 정상적으로 저장되어야 한다")
    @Test
    void save_test() {
        assertAll(
            () -> assertThat(deleteHistory.getId()).isNotNull(),
            () -> assertThat(deleteHistory2.getId()).isNotNull()
        );
    }

    @DisplayName("DeleteHistory 를 조회하면 정상적으로 조회되어야 한다")
    @Test
    void find_test() {
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        assertThat(deleteHistories).hasSize(2);
    }
}
