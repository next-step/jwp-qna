package qnamission.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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