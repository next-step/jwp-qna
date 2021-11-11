package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    public DeleteHistoryRepositoryTest(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @DisplayName("DeleteHistory가 저장된다")
    @Test
    void testSave() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(savedDeleteHistory.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(savedDeleteHistory.getDeletedById()).isEqualTo(deleteHistory.getDeletedById())
        );
    }
}
