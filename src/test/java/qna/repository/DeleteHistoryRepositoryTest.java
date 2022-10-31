package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DeleteHistoryTest.D1;
import static qna.domain.DeleteHistoryTest.D2;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAllInBatch();
    }

    @Test
    void 삭제이력_저장_테스트() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    void 삭제이력_저장_후_조회_테스트() {
        deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);
        List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();

        assertThat(findDeleteHistorys).hasSize(2);
    }

    @Test
    void 삭제이력_삭제_테스트() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);

        deleteHistoryRepository.delete(deleteHistory);
        List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();

        assertThat(findDeleteHistorys).hasSize(1);
    }
}
