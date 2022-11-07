package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Question 작성자 확인 테스트")
    void Question_작성자_확인(){
        assertAll(
                () -> assertTrue(Q1.isOwner(UserTest.JAVAJIGI)),
                () -> assertFalse(Q1.isOwner(UserTest.SANJIGI))
        );
    }

}
