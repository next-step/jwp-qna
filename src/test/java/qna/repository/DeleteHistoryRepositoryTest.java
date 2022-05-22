package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private UserRepository users;

    private DeleteHistory deleteHistory;

    @BeforeEach
    void setup() {
        User user = users.save(new User("sykim", "password", "sykim", "sykim@sykim.com"));
        deleteHistory = deleteHistories.save(new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now()));
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        assertThat(deleteHistories.findById(9999L)).isNotPresent();
    }

    @Test
    @DisplayName("저장")
    void create() {
        Optional<DeleteHistory> actual = deleteHistories.findById(deleteHistory.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        deleteHistories.delete(deleteHistory);
        assertThat(deleteHistories.findById(deleteHistory.getId())).isNotPresent();
    }
}
