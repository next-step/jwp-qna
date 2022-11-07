package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    private DeleteHistory D1;

    @BeforeEach
    void setup() {
        User JAVAJIGI = userRepository.save(JAVAJIGI());
        D1 = new DeleteHistory(ContentType.QUESTION, 1L, JAVAJIGI, LocalDateTime.now());
    }

    @Test
    void 저장() {
        DeleteHistory actual = deleteHistoryRepository.save(D1);
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isNotNull()
        );
    }
}
