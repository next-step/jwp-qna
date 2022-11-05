package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    public static final DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L,
            LocalDateTime.now());
    public static final DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.QUESTION, 2L, 1L,
            LocalDateTime.now());

    @Test
    void save() {
        assertAll(
                () -> assertDoesNotThrow(() -> deleteHistoryRepository.save(deleteHistory1)),
                () -> assertDoesNotThrow(() -> deleteHistoryRepository.save(deleteHistory2))
        );
    }
}
