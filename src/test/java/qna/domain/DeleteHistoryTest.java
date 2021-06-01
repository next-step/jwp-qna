package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteHistoryTest {
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User("tester", "password", "tester", "test@test.com");
    }

    @Test
    void createQuestionDeletion() {
        DeleteHistory.ofQuestion(1L, user1);
    }

    @Test
    void createAnswerDeletion() {
        DeleteHistory.ofAnswer(1L, user1);
    }
}
