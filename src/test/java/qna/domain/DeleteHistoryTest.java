package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("저장한_객체와_저장된_객체_비교")
    @Test
    void 저장한_객체와_저장된_객체_비교() {
        DeleteHistory deleteHistory = new DeleteHistory(1L, ContentType.QUESTION, LocalDateTime.now());

        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
        assertThat(actual).isEqualTo(deleteHistory);
    }
}
