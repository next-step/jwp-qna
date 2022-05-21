package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    private DeleteHistory deletedHistory;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deletedHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    }

    @DisplayName("identityTest 테스트")
    @Test
    void identityTest() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(deletedHistory);
        assertThat(deletedHistory).isSameAs(deleteHistory);
    }

    @DisplayName("검색 테스트")
    @Test
    void findByContentTypeTest() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(deletedHistory);
        Optional<DeleteHistory> isDeleteHistory = deleteHistoryRepository.findById(deletedHistory.getId());
        assertThat(isDeleteHistory.isPresent()).isTrue();
        assertThat(isDeleteHistory.get()).isSameAs(deleteHistory);
    }
}
