package qna.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("삭제이력을 새로 추가할 수 있다.")
    @Test
    void save_test() {
        DeleteHistory saved = deleteHistoryRepository.save(DeleteHistoryTest.D1);

        assertNotNull(saved.getId());
    }
}