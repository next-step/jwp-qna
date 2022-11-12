package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void 삭제이력_저장() {
        DeleteHistory actual = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory expected = deleteHistoryRepository.save(actual);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void 모든삭제이력_조회() {
        DeleteHistory actual = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        deleteHistoryRepository.save(actual);
        List<DeleteHistory> expected = deleteHistoryRepository.findAll();
        assertThat(expected).hasSize(1);
        assertThat(expected).contains(actual);
    }
}
