package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        deleteHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("deleteHistory 저장")
    void save_delete_history() {
        User user = userRepository.save(JAVAJIGI);
        DeleteHistory target = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
        deleteHistoryRepository.save(target);

        List<DeleteHistory> savedHistories = deleteHistoryRepository.findAll();

        assertThat(savedHistories.size()).isEqualTo(1);
    }
}
