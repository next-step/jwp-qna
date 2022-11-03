package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.UserTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAll();
    }

    @DisplayName("삭제 이력을 저장 후 확인")
    @Test
    void save() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L,
            UserTest.JAVAJIGI.getId(),
            LocalDateTime.now());

        DeleteHistory result = deleteHistoryRepository.save(deleteHistory);

        assertAll(
            () -> assertThat(result.getId()).isNotNull(),
            () -> assertThat(result.getContentType()).isEqualTo(deleteHistory.getContentType()),
            () -> assertThat(result.getContentId()).isEqualTo(deleteHistory.getContentId()),
            () -> assertThat(result.getDeletedById()).isEqualTo(deleteHistory.getDeletedById())
        );
    }

    @DisplayName("삭제 이력을 저장 후 조회 확인")
    @Test
    void findAll() {
        DeleteHistory deleteHistory1 = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.QUESTION, 1L,
                UserTest.JAVAJIGI.getId(),
                LocalDateTime.now()));
        DeleteHistory deleteHistory2 = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.ANSWER, 2L,
                UserTest.SANJIGI.getId(), LocalDateTime.now()));

        List<DeleteHistory> result = deleteHistoryRepository.findAll();

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).contains(deleteHistory1, deleteHistory2)
        );
    }

    @DisplayName("삭제 이력을 저장 후 삭제 확인")
    @Test
    void remove() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.QUESTION, 1L,
                UserTest.JAVAJIGI.getId(),
                LocalDateTime.now()));
        deleteHistoryRepository.delete(deleteHistory);

        Optional<DeleteHistory> result = deleteHistoryRepository.findById(deleteHistory.getId());

        assertThat(result).isNotPresent();
    }
}