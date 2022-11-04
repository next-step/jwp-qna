package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자 동일한지 테스트")
    void is_owner_check() {
        Question question = new Question("test", "test");
        question.writeBy(UserTest.JAVAJIGI);
        assertTrue(question.isOwner(UserTest.JAVAJIGI));
    }
}
