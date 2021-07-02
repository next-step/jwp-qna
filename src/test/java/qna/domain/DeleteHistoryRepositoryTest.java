package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Test
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 0L, 0L, LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findById() {
        deleteHistories.save(new DeleteHistory(ContentType.ANSWER, 0L, 0L, LocalDateTime.now()));
        Optional<DeleteHistory> actual = deleteHistories.findById(0L);
        assertThat(actual).isNotNull();
    }

}
