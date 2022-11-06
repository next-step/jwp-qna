package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    DeleteHistory questionHistory;
    DeleteHistory answerHistory;

    @BeforeEach
    void setUp() {
        User deletedBy = new User("user", "password", "name", "email@email.com");
        questionHistory = new DeleteHistory(ContentType.QUESTION, 1L, deletedBy);
        answerHistory = new DeleteHistory(ContentType.ANSWER, 1L, deletedBy);
    }

    @Test
    @DisplayName("삭제 이력 저장")
    void save_delete_history() {
        DeleteHistory actual = deleteHistoryRepository.save(questionHistory);
        DeleteHistory actual2 = deleteHistoryRepository.save(answerHistory);
        assertAll(
                () -> assertEquals(questionHistory, actual),
                () -> assertEquals(answerHistory, actual2)
        );
    }
}
