package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @AfterEach
    void clear() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);

        assertThat(expected).isEqualTo(actual);
    }
}
