package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save_deleteHistory() {
        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

        // when
        DeleteHistory expectDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        // then
        assertAll(
            () -> assertThat(expectDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }

    @Test
    void read_deleteHistory() {
        // given
        List<DeleteHistory> deleteHistory = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, 2L, 1L, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, 3L, 1L, LocalDateTime.now()),
            new DeleteHistory(ContentType.QUESTION, 4L, 1L, LocalDateTime.now())
        );
        deleteHistoryRepository.saveAll(deleteHistory);

        // when
        List<DeleteHistory> expectDeleteHistory = deleteHistoryRepository.findAll();

        // then
        assertAll(
            () -> assertThat(expectDeleteHistory).hasSize(deleteHistory.size()),
            () -> assertThat(expectDeleteHistory).containsAll(deleteHistory)
        );
    }

    @Test
    void delete_deleteHistory() {
        // given
        List<DeleteHistory> deleteHistory = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, 2L, 1L, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, 3L, 1L, LocalDateTime.now()),
            new DeleteHistory(ContentType.QUESTION, 4L, 1L, LocalDateTime.now())
        );
        List<DeleteHistory> expectDeleteHistory = deleteHistoryRepository.saveAll(deleteHistory);

        // when
        deleteHistoryRepository.deleteAll();

        List<DeleteHistory> deletedHistoryList = deleteHistoryRepository.findAll();

        // then
        assertAll(
            () -> assertThat(expectDeleteHistory).hasSize(deleteHistory.size()),
            () -> assertThat(deletedHistoryList).hasSize(0)
        );
    }
}