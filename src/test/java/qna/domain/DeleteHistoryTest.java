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
    private DeleteHistoryRepository deleteHistories;

    @Test
    @DisplayName("save 확인")
    void save() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
        DeleteHistory result = deleteHistories.save(deleteHistory);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getCreateDate()).isEqualTo(deleteHistory.getCreateDate()),
                () -> assertThat(result.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(result.getDeletedById()).isEqualTo(deleteHistory.getDeletedById())
        );
    }

}