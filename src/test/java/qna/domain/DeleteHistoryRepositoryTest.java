package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("삭제 이력 테스트")
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("삭제 이력 저장 확인")
    void create() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(DeleteHistoryTest.D1);

        assertThat(savedDeleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("저장한 삭제 이력과 해당 삭제 이력이 같은지 확인")
    void read() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(DeleteHistoryTest.D1);
        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        assertThat(savedDeleteHistory).isEqualTo(findDeleteHistory.get());
    }

    @Test
    @DisplayName("삭제 이력 조회 개수 확인")
    void find_all_count() {
        DeleteHistory savedDeleteHistory1 = deleteHistoryRepository.save(DeleteHistoryTest.D1);
        DeleteHistory savedDeleteHistory2 = deleteHistoryRepository.save(DeleteHistoryTest.D2);

        List<DeleteHistory> deleteHistoryList = deleteHistoryRepository.findAll();

        assertThat(deleteHistoryList).hasSize(2);
        assertThat(deleteHistoryList).containsExactlyInAnyOrder(savedDeleteHistory1, savedDeleteHistory2);
    }

    @Test
    @DisplayName("삭제 이력 삭제 확인")
    void delete() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(DeleteHistoryTest.D1);

        deleteHistoryRepository.delete(savedDeleteHistory);

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        assertThat(findDeleteHistory).isEmpty();
        assertThat(findDeleteHistory).isNotPresent();
    }

}
