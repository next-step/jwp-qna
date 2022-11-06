package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    @DisplayName("저장한 DeleteHistory 의 id 가 null 이 아닌지 확인")
    void create() {
        User deleter = new User("user1", "password", "name", "test@email.com");
        userRepository.save(deleter);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertThat(savedDeleteHistory.getId()).isNotNull();
    }

    @Transactional
    @Test
    @DisplayName("(findById 테스트) 저장한 DeleteHistory 와 조회한 DeleteHistory 가 같은지(동일성) 확인")
    void read() {
        User deleter = new User("user1", "password", "name", "test@email.com");
        userRepository.save(deleter);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now());
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        assertAll(
                () -> assertThat(findDeleteHistory).isPresent(),
                () -> assertThat(savedDeleteHistory).isSameAs(findDeleteHistory.get())
        );
    }

    @Transactional
    @Test
    @DisplayName("(findAll 테스트) 저장한 DeleteHistory 와 조회한 DeleteHistory 가 같은지(동일성) 확인")
    void read2() {
        User deleter1 = new User("user1", "password", "name", "test@email.com");
        User deleter2 = new User("user2", "password", "name", "test@email.com");
        userRepository.save(deleter1);
        userRepository.save(deleter2);

        DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, 1L, deleter1, LocalDateTime.now());
        DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 2L, deleter2, LocalDateTime.now());
        DeleteHistory savedDeleteHistory1 = deleteHistoryRepository.save(deleteHistory1);
        DeleteHistory savedDeleteHistory2 = deleteHistoryRepository.save(deleteHistory2);

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

        assertAll(
                () -> assertThat(deleteHistories).hasSize(2),
                () -> assertThat(deleteHistories).containsExactlyInAnyOrder(savedDeleteHistory1, savedDeleteHistory2)
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 deleteHistory 삭제 후 조회 시 deleteHistory 가 없는지 확인")
    void delete() {
        User deleter = new User("user1", "password", "name", "test@email.com");
        userRepository.save(deleter);

        DeleteHistory deleteHistory =
                deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, deleter, LocalDateTime.now()));
        deleteHistoryRepository.delete(deleteHistory);

        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId());

        Assertions.assertThat(findDeleteHistory).isNotPresent();
    }
}
