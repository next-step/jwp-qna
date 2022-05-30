package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    User savedUser;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        savedUser = new User("deletejigi", "password", "DELETEJIGI", "deletejigi@slipp.net");
        userRepository.save(savedUser);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("저장된 DeleteHistory와 실제 저장한 DeleteHistory가 동일해야 한다.")
    public void saveTest() {
        // given
        DeleteHistory history = new DeleteHistory(ContentType.QUESTION, 1L, savedUser,
            LocalDateTime.now());
        DeleteHistory savedHistory = deleteHistoryRepository.save(history);

        // when
        DeleteHistory foundHistory = deleteHistoryRepository.findById(1L)
            .orElse(null);

        // then
        assertThat(foundHistory).isEqualTo(savedHistory);
    }
}
