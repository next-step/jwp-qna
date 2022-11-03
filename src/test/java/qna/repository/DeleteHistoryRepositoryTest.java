package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.UserTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("DeleteHistory 저장 테스트")
    void saveDeleteHistory() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI.getId(), LocalDateTime.now());

        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertAll(
                () -> assertThat(saveDeleteHistory.getId()).isNotNull(),
                () -> assertThat(saveDeleteHistory.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(saveDeleteHistory.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(saveDeleteHistory.getDeletedById()).isEqualTo(deleteHistory.getDeletedById())
        );
    }

    @Test
    @DisplayName("DeleteHistory 2건 저장 후 전체 조회 테스트")
    void saveAllDeleteHistory() {
        DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI.getId(), LocalDateTime.now());
        DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI.getId(), LocalDateTime.now());
        deleteHistoryRepository.saveAll(Arrays.asList(deleteHistory1, deleteHistory2));

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

        assertThat(deleteHistories).hasSize(2);
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 DeleteHistory 조회 테스트")
    void readDeleteHistory() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI.getId(), LocalDateTime.now());
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistory.getId());

        assertThat(findDeleteHistory).isPresent();
        assertThat(findDeleteHistory.get()).isSameAs(saveDeleteHistory);
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 DeleteHistory 삭제 테스트")
    void deleteDeleteHistory() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI.getId(), LocalDateTime.now());
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        deleteHistoryRepository.delete(saveDeleteHistory);

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistory.getId());

        assertThat(findDeleteHistory).isNotPresent();
    }
}
