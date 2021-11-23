package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {
    private Answer answer;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "userId", "password", "name", "email");
        Question question = new Question(1L, "title", "contents").writeBy(user);
        answer = new Answer(user, question, "Answers Contents1");
    }

    @DisplayName("작성자 불일치")
    @Test
    void validateOwner_fail() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answer.validateOwner(UserTest.SANJIGI))
            .withMessage("답변을 삭제할 권한이 없습니다.");
    }

    @DisplayName("삭제")
    @Test
    void delete() {
        assertThat(answer.delete())
            .isEqualTo(DeleteHistory.ofContent(Content.ofAnswer(answer)));
    }
}
