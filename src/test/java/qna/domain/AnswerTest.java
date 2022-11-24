package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("id 비교 테스트")
    @Test
    void id_match() {
        User user = new User();
        User user2 = new User();
        User user3 = new User();
        String userId = "testUser";
        user.setUserId(userId);
        user2.setUserId(userId);
        user3.setUserId("testUser3");
        assertAll(
                () -> assertThat(user.matchUserId(user2)).isTrue(),
                () -> assertThat(user.matchUserId(user3)).isFalse()
        );

    }
}
