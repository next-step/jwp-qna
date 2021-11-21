package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerTest {
    private Answer answer;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "userId", "password", "name", "email");
        Question question = new Question(1L, "title", "contents").writeBy(user);
        answer = new Answer(user, question, "Answers Contents1");
    }

    @DisplayName("작성자 일치")
    @Test
    void validateOwner_true() {
        assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("작성자 불일치")
    @Test
    void validateOwner_false() {
        assertThat(answer.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @DisplayName("삭제")
    @Test
    void delete() {
        assertThat(answer.delete())
            .isEqualTo(new DeleteHistory(new Content(answer), LocalDateTime.now()));
    }
}
