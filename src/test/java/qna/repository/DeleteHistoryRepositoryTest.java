package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory save() 테스트를 진행한다")
    void saveHistory() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        DeleteHistory history = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());

        DeleteHistory result = deleteHistoryRepository.save(history);

        assertThat(history).isEqualTo(result);
    }

    @Test
    @DisplayName("저장한 삭제 이력과 해당 삭제 이력이 같은지 확인")
    void saveHistoryAndFind() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        DeleteHistory history = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now()));

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(history.getId());

        assertThat(history).isEqualTo(findDeleteHistory.get());
    }

    @Test
    @DisplayName("모든삭제 히스토리 데이터를 가져온다")
    void allHistory() {
        User userA = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        User userB = userRepository.save(new User(2L, "javajigi2", "password2", "name2", "javajigi@slipp.com"));
        DeleteHistory historyA = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 1L, userA, LocalDateTime.now()));
        DeleteHistory historyB = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 2L, userB, LocalDateTime.now()));

        List<DeleteHistory> result = deleteHistoryRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(historyA, historyB);
    }

    @Test
    @DisplayName("삭제 이력 삭제 확인")
    void deleteHistory() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        DeleteHistory history = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now()));

        deleteHistoryRepository.delete(history);

        Optional<DeleteHistory> result = deleteHistoryRepository.findById(history.getId());

        assertThat(result).isEmpty();
        assertThat(result).isNotPresent();
    }

}