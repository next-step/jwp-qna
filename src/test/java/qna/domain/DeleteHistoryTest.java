package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryTest {
    public static final DeleteHistory D1
            = new DeleteHistory(ContentType.QUESTION, 1L, 2L, LocalDateTime.now());
    @Autowired
    DeleteHistoryRepository deleteHistory;

    @Test
    void save() {
        DeleteHistory actual = deleteHistory.save(D1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isSameAs(D1)
        );

    }
}