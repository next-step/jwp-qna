package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    private User user1;
    private User user2;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user1 = UserTest.JAVAJIGI;
        user2 = UserTest.SANJIGI;

        answer = new Answer(user1, QuestionTest.Q1, "Answers Contents1");
    }

    @DisplayName("작성자라면 True를 리턴합니다")
    @Test
    void isOwner_true() {
        assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("작성자라면 False를 리턴합니다")
    @Test
    void isOwner_false() {
        assertThat(answer.isOwner(user2)).isFalse();
    }

    @DisplayName("answer를 삭제한다")
    @Test
    void delete() {
        answer.delete(user1);
        assertThat(answer.isDeleted()).isTrue();
    }
}
