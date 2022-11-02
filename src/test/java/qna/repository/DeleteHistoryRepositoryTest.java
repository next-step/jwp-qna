package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
@DisplayName("DeleteHistory")
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @Test
    @DisplayName("저장")
    public void save() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory saved = repository.save(deleteHistory);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory saved = repository.save(deleteHistory);
        Optional<DeleteHistory> fetched = repository.findById(saved.getId());
        assertThat(fetched).isNotEmpty();
        assertThat(fetched.get()).isEqualTo(saved);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory saved = repository.save(deleteHistory);
        repository.delete(saved);
        Optional<DeleteHistory> fetched = repository.findById(saved.getId());
        assertThat(fetched).isEmpty();
    }
}
