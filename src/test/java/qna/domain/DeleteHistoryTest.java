package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("삭제 내역 저장 성공 처리 검증")
    void save() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

        assertAll(
                () -> deleteHistoryRepository.save(deleteHistory)
        );
    }
}