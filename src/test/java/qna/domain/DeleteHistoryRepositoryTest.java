package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistorys;

    @DisplayName("삭제이력 저장")
    @Test
    void save() {
        DeleteHistory deleteHistory
            = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        deleteHistory = deleteHistorys.save(deleteHistory);

        Long deleteHistoryId = deleteHistory.getId();

        assertThat(deleteHistoryId).isNotNull();
    }
}
