package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 7L, 8L, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType())
        );
    }
}
