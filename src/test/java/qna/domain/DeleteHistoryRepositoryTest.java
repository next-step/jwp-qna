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
        User writer = new User("id", "password", "name", "email");
        DeleteHistory expected = DeleteHistory.ofAnswer(0L, writer);
        DeleteHistory actual = deleteHistories.save(expected);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findById() {
        User writer = new User("id", "password", "name", "email");
        deleteHistories.save(DeleteHistory.ofAnswer(0L, writer));
        Optional<DeleteHistory> actual = deleteHistories.findById(0L);
        assertThat(actual).isNotNull();
    }

}
