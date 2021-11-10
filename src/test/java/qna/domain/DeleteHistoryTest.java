package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistory;
    
    @Test
    @DisplayName("저장됐는지 확인")
    void 저장() {
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.of(2021,  11, 10, 1, 1, 1));
        DeleteHistory actual = deleteHistory.save(expected);
        assertThat(actual).isEqualTo(expected);
    }
}
