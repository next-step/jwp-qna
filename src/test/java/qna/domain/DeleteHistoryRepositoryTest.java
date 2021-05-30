package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(DeleteHistoryTest.DELETE_HISTORY1);
        DeleteHistory deleteHistory2 = deleteHistoryRepository.save(DeleteHistoryTest.DELETE_HISTORY2);
    }
}
