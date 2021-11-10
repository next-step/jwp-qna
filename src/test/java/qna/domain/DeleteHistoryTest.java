package qna.domain;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("DeleteHistory 를 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
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