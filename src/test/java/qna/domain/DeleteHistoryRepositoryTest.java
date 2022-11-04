package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void 저장() {
        DeleteHistory actual = deleteHistoryRepository.save(D1);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
    }
}
