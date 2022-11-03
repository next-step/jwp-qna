package qna.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class DeleteHistoryTest {

    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(D1);

        assertNotNull(savedDeleteHistory.getId());
    }
}
