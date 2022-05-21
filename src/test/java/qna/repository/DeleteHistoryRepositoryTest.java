package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.repository.entity.DeleteHistory;
import qna.repository.entity.User;
import qna.repository.entity.UserTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("삭제 이력을 저장한다")
    void save() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, javajigi, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);

        assertThat(expected).isEqualTo(actual);
    }
}
