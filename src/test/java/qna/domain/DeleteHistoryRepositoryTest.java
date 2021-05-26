package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory 저장 테스트")
    void save() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

        DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);

        assertThat(saved).isEqualTo(deleteHistory);
    }

    @Test
    @DisplayName("DeleteHistory 제거 테스트")
    void delete() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);

        deleteHistoryRepository.delete(saved);

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

        assertThat(deleteHistories).isEmpty();
    }

    @Test
    @DisplayName("DeleteHistory 조회 테스트")
    void find() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);

        Optional<DeleteHistory> finded = deleteHistoryRepository.findById(saved.getId());

        assertAll(
                () -> assertThat(finded.isPresent()).isTrue(),
                () -> assertThat(finded.get()).isEqualTo(saved)
        );
    }
}
