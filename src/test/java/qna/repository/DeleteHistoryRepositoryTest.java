package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save() {
        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

        // when
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        // then
        assertThat(actual).isNotNull();
    }
}
