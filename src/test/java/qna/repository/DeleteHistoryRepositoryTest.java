package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
@DisplayName("DeleteHistory")
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static DeleteHistory deleteHistory;

    @BeforeAll
    public static void init() {
        deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    }

    @Test
    @DisplayName("저장")
    public void save() {
        DeleteHistory saved = saveAndRefetch(deleteHistory);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        DeleteHistory saved = saveAndClear(deleteHistory);
        Optional<DeleteHistory> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        DeleteHistory fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        DeleteHistory saved = saveAndRefetch(deleteHistory);
        repository.delete(saved);
        Optional<DeleteHistory> optional = repository.findById(saved.getId());
        assertThat(optional).isEmpty();
    }

    private DeleteHistory saveAndRefetch(DeleteHistory deleteHistory) {
        DeleteHistory saved = saveAndClear(deleteHistory);
        return repository.findById(saved.getId())
                .orElseThrow(() -> new NullPointerException("DeleteHistory not saved!"));
    }

    private DeleteHistory saveAndClear(DeleteHistory deleteHistory) {
        DeleteHistory saved = repository.save(deleteHistory);
        testEntityManager.clear();
        return saved;
    }
}
