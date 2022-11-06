package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    public DeleteHistory deleteHistory;
    public User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        deleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));
    }

    @DisplayName("Id로 조회 검증")
    @Test
    void findById() {
        DeleteHistory result = deleteHistoryRepository.findById(deleteHistory.getId()).orElse(null);
        assertThat(result).isEqualTo(deleteHistory);
    }

    @DisplayName("delete 검증")
    @Test
    void delete() {
        deleteHistoryRepository.delete(deleteHistory);
        Optional<DeleteHistory> result = deleteHistoryRepository.findById(deleteHistory.getId());
        assertThat(result).isNotPresent();
    }

    @DisplayName("findByDeletedBy 검증")
    @Test
    void findByDeletedBy() {
        List<DeleteHistory> result = deleteHistoryRepository.findByDeletedBy(user);
        assertThat(result.contains(deleteHistory)).isTrue();
    }

}
