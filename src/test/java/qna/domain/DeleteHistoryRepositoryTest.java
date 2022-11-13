package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    UserRepository userRepository;

    private static User user;

    @BeforeEach
    void init() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    private static final DeleteHistory questionDeletedHistory = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
    private static final DeleteHistory answerDeletedHistory = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());


    @Test
    @DisplayName("Deleted Histroy 저장")
    void deleted_history_repository_save() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(questionDeletedHistory);
        assertThat(deleteHistory.getId()).isNotNull();
    }
}
