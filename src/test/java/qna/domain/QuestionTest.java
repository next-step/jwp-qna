package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuestionTest {
    public static final String Q1_TITLE = "title1";
    public static final String Q1_CONTENT = "contents1";

    public static final String Q2_TITLE = "title2";
    public static final String Q2_CONTENT = "contents2";

    public static final Question Q1 = new Question(Q1_TITLE, Q1_CONTENT).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(Q2_TITLE, Q2_CONTENT).writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("게시판을 올린 사용자가 맞는지 검증한다.")
    void validateIsOwner_test() {
        assertThrows(CannotDeleteException.class,
                () -> Q1.validateIsOwner(UserTest.SANJIGI)
        );
    }
}
