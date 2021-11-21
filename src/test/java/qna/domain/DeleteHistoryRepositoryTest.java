package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("삭제이력 저장")
    @Test
    void save() {
        User user = userRepository.save(new User("userId", "password", "name", "email"));
        DeleteHistory expected = new DeleteHistory(new Content(ContentType.ANSWER, 1L, user), LocalDateTime.now());

        DeleteHistory actual = deleteHistoryRepository.save(expected);

        assertThat(actual).isEqualTo(expected);
    }
}