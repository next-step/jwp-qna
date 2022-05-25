package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save() {
        final DeleteHistory expected = new DeleteHistory();
        final DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertNotNull(actual.getId());
    }

}