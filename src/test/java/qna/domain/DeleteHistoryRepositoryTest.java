package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private DeleteHistory deleteHistory;

    @BeforeEach
    public void setUp() {
        deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        deleteHistory = deleteHistoryRepository.save(deleteHistory);
    }

    @Test
    public void deleteHistorySave() {
        List<DeleteHistory> deleteHistorys = deleteHistoryRepository.findAll();

        assertAll(
                () -> assertThat(deleteHistorys).isNotNull(),
                () -> assertThat(deleteHistorys).hasSize(1),
                () -> assertThat(deleteHistorys).contains(deleteHistory)
        );
    }

    @Test
    public void deleteHistoryFind() {
        DeleteHistory expectValue = deleteHistoryRepository.findById(1L).orElse(null);

        assertAll(
                () -> assertThat(expectValue).isNotNull(),
                () -> assertThat(expectValue).isEqualTo(deleteHistory)
        );


    }

}