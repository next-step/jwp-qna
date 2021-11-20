package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistorys;

    @Test
    void init() {
        DeleteHistory deleteHistory
            = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());

        assertAll(
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(1L),
            () -> assertThat(deleteHistory.getDeleter()).isEqualTo(UserTest.JAVAJIGI)
        );
    }
}
