package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory 저장 확인")
    void saveTest() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
        DeleteHistory result = deleteHistoryRepository.save(deleteHistory);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(result.getDeletedById()).isEqualTo(deleteHistory.getDeletedById()),
                () -> assertThat(result.getCreateDate()).isEqualTo(deleteHistory.getCreateDate()),
                () -> assertThat(result.getContentType()).isEqualTo(deleteHistory.getContentType())
        );
    }
}