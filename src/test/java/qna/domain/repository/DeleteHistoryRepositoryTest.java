package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertThat(expected).isEqualTo(actual);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType()),
                () -> assertThat(actual.getDeletedById()).isEqualTo(expected.getDeletedById())
        );
    }

    @Test
    void findById() {
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(1L).get();
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
