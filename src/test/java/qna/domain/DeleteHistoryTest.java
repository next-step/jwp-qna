package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("identityTest 테스트")
    @Test
    void identityTest() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory deleteHistory = deleteHistoryRepository.save(history);
        assertThat(history).isSameAs(deleteHistory);
    }

    @DisplayName("검색 테스트")
    @Test
    void findByContentTypeTest() {
        DeleteHistory history = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory deleteHistory = deleteHistoryRepository.save(history);
        List<DeleteHistory> deleteHistoryList = deleteHistoryRepository.findByContentType(ContentType.ANSWER);
        assertThat(deleteHistoryList).contains(deleteHistory);
    }
}
