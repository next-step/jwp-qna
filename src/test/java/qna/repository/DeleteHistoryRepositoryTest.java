package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.UserTest.JAVAJIGI;

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
import qna.domain.DeleteHistoryRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    private DeleteHistory deleteHistory;
    private DeleteHistory deleteHistory2;
    private User deletedBy;

    @BeforeEach
    void setUp() {
        deletedBy = userRepository.save(JAVAJIGI);
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deletedBy,
            LocalDateTime.now());
        deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 1L, deletedBy,
            LocalDateTime.now());
    }

    @DisplayName("DeleteHistory 를 저장하면 정상적으로 저장되어야 한다")
    @Test
    void save_test() {
        deleteHistoryRepository.save(deleteHistory);
        deleteHistoryRepository.save(deleteHistory2);

        assertAll(
            () -> assertThat(deleteHistory.getId()).isNotNull(),
            () -> assertThat(deleteHistory2.getId()).isNotNull()
        );
    }

    @DisplayName("DeleteHistory 를 조회하면 정상적으로 조회되어야 한다")
    @Test
    void find_test() {
        deleteHistoryRepository.save(deleteHistory);
        deleteHistoryRepository.save(deleteHistory2);

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        assertThat(deleteHistories).hasSize(2);
    }

    @DisplayName("DeleteHistory 를 조회하면 연관관계인 deletedBy 도 같이 저장되어야 한다")
    @Test
    void save_relation_test() {
        deleteHistory = deleteHistoryRepository.save(deleteHistory);

        Optional<DeleteHistory> find_deleteHistory = deleteHistoryRepository.findById(deleteHistory.getId());

        assertTrue(find_deleteHistory.isPresent());
        assertThat(find_deleteHistory.get()).isNotNull();
        assertThat(find_deleteHistory.get().getDeletedBy()).isNotNull();
    }

    @DisplayName("DeleteHistory 를 조회하면 연관관계인 deletedBy 도 같이 저장되어야 한다")
    @Test
    void find_relation_test() {
        deleteHistory = deleteHistoryRepository.save(deleteHistory);

        DeleteHistory find_deleteHistory = deleteHistoryRepository.findById(deleteHistory.getId()).get();
        User 삭제한_유저 = find_deleteHistory.getDeletedBy();

        assertThat(삭제한_유저).isNotNull();
        assertThat(삭제한_유저.getId()).isEqualTo(deletedBy.getId());
        assertThat(삭제한_유저.getUserId()).isEqualTo(deletedBy.getUserId());
    }
}
