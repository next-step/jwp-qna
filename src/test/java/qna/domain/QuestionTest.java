package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("delete 성공")
    @Test
    void deleteTest01() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        assertDoesNotThrow(() -> {
            question.delete(UserTest.JAVAJIGI);
        });
    }

    @DisplayName("delete가 유저가 같지 않아서 실패하는 경우 테스트")
    @Test
    void deleteTest02() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> {
            question.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("이미 삭제된 질문을 삭제시 exception 발생")
    @Test
    void deleteTest03() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        assertDoesNotThrow(() -> {
            question.delete(UserTest.JAVAJIGI);
        });

        assertThatThrownBy(() -> {
            question.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
