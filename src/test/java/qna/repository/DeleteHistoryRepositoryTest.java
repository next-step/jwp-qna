package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.DeleteHistoryTest.D1;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAllInBatch();
    }

    @Test
    void deleteHistory_save_test() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);
        assertThat(deleteHistory.getId()).isNotNull();
    }
}