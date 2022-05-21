package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.AnswerTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.DeleteHistoryTest;
import qna.domain.UserTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Test
    @DisplayName("저장")
    void create() {
        DeleteHistory savedDeleteHistory = deleteHistories.save(DeleteHistoryTest.D1);
        Optional<DeleteHistory> actual = deleteHistories.findById(savedDeleteHistory.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(),
                UserTest.JAVAJIGI.getId(), LocalDateTime.now());
        deleteHistories.save(deleteHistory);
        Optional<DeleteHistory> actual = deleteHistories.findById(9999L);
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(),
                UserTest.JAVAJIGI.getId(), LocalDateTime.now());
        deleteHistories.save(deleteHistory);
        Optional<DeleteHistory> actual = deleteHistories.findById(deleteHistory.getId());
        assertThat(actual).isPresent();

        deleteHistories.deleteById(actual.get().getId());
        actual = deleteHistories.findById(actual.get().getId());
        assertThat(actual).isNotPresent();
    }
}
