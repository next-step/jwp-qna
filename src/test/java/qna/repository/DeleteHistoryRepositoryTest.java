package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Test
    @DisplayName("delete history 내용을 저장한다.")
    void save_delete_history() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
        DeleteHistory expect = deleteHistories.save(deleteHistory);
        assertThat(deleteHistory).isEqualTo(expect);
    }

    @Test
    @DisplayName("delete history 내용을 삭제한다")
    void delete_delete_history() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
        DeleteHistory expect = deleteHistories.save(deleteHistory);
        deleteHistories.delete(expect);
        assertThat(deleteHistories.findById(expect.getId())).isEmpty();
    }

    @Test
    @DisplayName("delete history 내용을 조회한다(1건 저장했기에 1건 조회)")
    void find_delete_history() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
        deleteHistories.save(deleteHistory);
        assertThat(deleteHistories.findAll()).hasSize(1);
    }
}
