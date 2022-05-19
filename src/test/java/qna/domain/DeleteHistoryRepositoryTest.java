package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("삭제 이력을 저장한다.")
    @Test
    void save() {
        User javaJigi = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory
            = new DeleteHistory(ContentType.QUESTION, 1L, javaJigi);
        DeleteHistory history = deleteHistoryRepository.save(deleteHistory);
        assertAll(
            () -> assertThat(history).isNotNull()
        );
    }
}