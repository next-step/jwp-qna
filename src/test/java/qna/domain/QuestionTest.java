package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

}
