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
        final ContentType contentType = ContentType.ANSWER;
        final Long contentId = 1L;
        final Long deletedById = 1L;
        final LocalDateTime createDate = LocalDateTime.now();
        final DeleteHistory deleteHistory = new DeleteHistory(contentType, contentId, deletedById, createDate);
        // when
        final DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        // then
        assertEquals(savedDeleteHistory, deleteHistory);
    }
}