package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.QnaDataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@QnaDataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void id로_조회한다() {
        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);
        // when
        Optional<DeleteHistory> result = deleteHistoryRepository.findById(saved.getId());
        // then
        assertThat(result)
                .map(DeleteHistory::getContentType)
                .hasValue(ContentType.QUESTION);
    }
}