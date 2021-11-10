package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
public class DeleteHistoryTest {

    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    public DeleteHistoryTest(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Test
    void testEquals() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        assertThat(savedDeleteHistory.getId()).isNotNull();
        assertThat(savedDeleteHistory.getContentType()).isEqualTo(deleteHistory.getContentType());
        assertThat(savedDeleteHistory.getContentId()).isEqualTo(deleteHistory.getContentId());
        assertThat(savedDeleteHistory.getDeletedById()).isEqualTo(deleteHistory.getDeletedById());
        assertThat(savedDeleteHistory.getCreateDate()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }
}
