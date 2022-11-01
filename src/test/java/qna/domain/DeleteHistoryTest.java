package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("삭제 이력 테스트")
class DeleteHistoryTest {

    @Test
    void user_동등성_테스트() {
        assertAll(
                () -> assertEquals(
                        new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()),
                        new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now())
                ),
                () -> assertNotEquals(
                        new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()),
                        new DeleteHistory(ContentType.ANSWER, 2L, 1L, LocalDateTime.now())
                )
        );
    }

}
