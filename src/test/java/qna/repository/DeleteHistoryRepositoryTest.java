package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;
import qna.domain.UserTest;
import qna.repository.DeleteHistoryRepository;
import qna.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("삭제 이력 테스트")
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("삭제 이력 저장 확인")
    void create() {
        User deleter = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertThat(savedDeleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("저장한 삭제 이력과 해당 삭제 이력이 같은지 확인")
    void read() {
        User deleter = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        assertThat(savedDeleteHistory).isEqualTo(findDeleteHistory.get());
    }

    @Test
    @DisplayName("삭제 이력 조회 개수 확인")
    void find_all_count() {
        User deleter = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory answerDeleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now());
        DeleteHistory questionDeleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, deleter, LocalDateTime.now());
        DeleteHistory savedDeleteHistory1 = deleteHistoryRepository.save(answerDeleteHistory);
        DeleteHistory savedDeleteHistory2 = deleteHistoryRepository.save(questionDeleteHistory);

        List<DeleteHistory> deleteHistoryList = deleteHistoryRepository.findAll();

        assertThat(deleteHistoryList).hasSize(2);
        assertThat(deleteHistoryList).containsExactlyInAnyOrder(savedDeleteHistory1, savedDeleteHistory2);
    }

    @Test
    @DisplayName("삭제 이력 삭제 확인")
    void delete() {
        User deleter = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        deleteHistoryRepository.delete(savedDeleteHistory);

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        assertThat(findDeleteHistory).isEmpty();
        assertThat(findDeleteHistory).isNotPresent();
    }

}
