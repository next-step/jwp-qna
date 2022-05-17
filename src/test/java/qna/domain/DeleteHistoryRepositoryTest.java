package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, now());
        deleteHistoryRepository.save(deleteHistory);
    }

    @Test
    void 생성() {
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    void 조회() {
        DeleteHistory findHistory = deleteHistoryRepository.findById(deleteHistory.getId()).get();
        assertThat(findHistory).isSameAs(deleteHistory);
    }

    @Test
    void 삭제() {
        deleteHistoryRepository.delete(deleteHistory);
        assertThat(deleteHistoryRepository.findById(deleteHistory.getId())).isNotPresent();
    }
}
