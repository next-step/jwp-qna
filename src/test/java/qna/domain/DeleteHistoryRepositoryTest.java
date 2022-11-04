package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.domain.DeleteHistoryTestFixture.AH;
import static qna.domain.DeleteHistoryTestFixture.QH;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("삭제 이력 저장")
    void save_delete_history() {
        DeleteHistory questionHistory = deleteHistoryRepository.save(QH);
        DeleteHistory answerHistory = deleteHistoryRepository.save(AH);
        assertAll(
                () -> assertEquals(QH, questionHistory),
                () -> assertEquals(AH, answerHistory)
        );
    }
}
