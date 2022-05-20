package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Test
    @DisplayName("작성자가 아닌 다른사람이 삭제 하면 예외 발생")
    void deleteTest() {
        assertThatThrownBy(
                () -> Q1.delete(SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("Question 작성자와 Answer 작성자가 다르면 Question 삭제 시 예외 발생")
    void deleteTest2() {
        Q1.addAnswer(A2);
        assertThatThrownBy(
                () -> Q1.delete(JAVAJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }
}
