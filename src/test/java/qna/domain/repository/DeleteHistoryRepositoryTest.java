package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.QuestionTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    private DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, QuestionTest.Q1.getWriterId(), LocalDateTime.now());
    private DeleteHistory savedDeleteHistory;
    
    @BeforeEach
    void saveDeleteHistory() {
        savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
    }
    
    @Test
    @DisplayName("저장")
    void saveTest() {
        assertAll(
            () -> assertThat(deleteHistoryRepository.count()).isEqualTo(1),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(savedDeleteHistory.getContentId())
        );
    }
    
    @Test
    @DisplayName("삭제")
    void deleteTest() {
        deleteHistoryRepository.delete(savedDeleteHistory);
        assertAll(
            () -> assertThat(deleteHistoryRepository.count()).isEqualTo(0),
            () -> assertThat(deleteHistoryRepository.existsById(savedDeleteHistory.getContentId())).isFalse()
        );
    }
}
