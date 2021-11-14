package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryTest {

    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.ANSWER, 2L, 2L, LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);
    }

    @AfterEach
    void clean() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    void findById() {
        // given
        DeleteHistory deleteHistory = deleteHistoryRepository.findAll().get(0);

        // when
        DeleteHistory result = deleteHistoryRepository.findById(deleteHistory.getId()).get();

        // then
        assertThat(result).isEqualTo(deleteHistory);
    }

    @Test
    void remove() {
        // given
        List<DeleteHistory> prevResult = deleteHistoryRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        deleteHistoryRepository.deleteAll();

        // then
        List<DeleteHistory> result = deleteHistoryRepository.findAll();
        assertThat(result).isEmpty();
    }

}