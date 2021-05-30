package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private DeleteHistory savedDeleteHistory;

    @BeforeEach
    void setUp() {
        DeleteHistory expect = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        savedDeleteHistory = deleteHistoryRepository.save(expect);
    }

    @DisplayName("저장")
    @Test
    public void saveDeleteHistory() {
        assertThat(savedDeleteHistory).isNotNull();
    }
}
