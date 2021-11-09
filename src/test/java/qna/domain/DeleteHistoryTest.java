package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save() {
        final LocalDateTime dateTime = LocalDateTime.of(2021, 11, 9, 0, 0, 0);
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, dateTime);

        final DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertThat(savedDeleteHistory).isEqualTo(deleteHistory);
    }
}
