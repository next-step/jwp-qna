package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.deletehistory.ContentType;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.deletehistory.DeleteHistoryRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void DeleteHistory_를_저장_할_경우_저장된_객체와_저장_후_객체가_일치하다() {
        // given
        final DeleteHistory deleteHistory = create(1L, 1L);
        // when
        final DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        // then
        assertEquals(savedDeleteHistory, deleteHistory);
    }

    private DeleteHistory create(Long contentId, Long deletedById) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedById, LocalDateTime.now());
    }
}