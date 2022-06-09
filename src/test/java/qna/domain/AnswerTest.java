package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("delete 성공")
    @Test
    void deleteTest01() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "test answer");
        assertDoesNotThrow(() -> {
            answer.delete(UserTest.JAVAJIGI);
        });
    }

    @DisplayName("delete가 유저가 같지 않아서 실패하는 경우 테스트")
    @Test
    void deleteTest02() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "test answer");
        assertThatThrownBy(() -> {
            answer.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("이미 삭제된 answer을 삭제시 exception 발생")
    @Test
    void deleteTest03() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "test answer");
        assertDoesNotThrow(() -> {
            answer.delete(UserTest.JAVAJIGI);
        });

        assertThatThrownBy(() -> {
            answer.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
