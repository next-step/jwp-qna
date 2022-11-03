package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void init() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("deleteHistory 저장")
    void save_delete_history() {
        DeleteHistory target = new DeleteHistory(ContentType.QUESTION, 1L, 2L, LocalDateTime.now());
        deleteHistoryRepository.save(target);

        List<DeleteHistory> savedHistories = deleteHistoryRepository.findAll();

        assertThat(savedHistories.size()).isEqualTo(1);
    }
}
