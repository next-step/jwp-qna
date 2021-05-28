package qna.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DeleteHistoryTest {
    private User user1 = new User("tester", "password", "tester", "test@test.com");
    @Test
    void canCreate() {
        new DeleteHistory(
                ContentType.QUESTION, 1L, user1, LocalDateTime.now());
    }
}
