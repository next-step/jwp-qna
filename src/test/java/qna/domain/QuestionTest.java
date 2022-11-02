package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("질문 클래스 테스트")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1")
                                        .writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2")
                                        .writeBy(UserTest.SANJIGI);

    @Test
    void 해당_유저가_작성한_질문인지_확인() {
        assertAll(
                () -> assertTrue(Q1.isOwner(UserTest.JAVAJIGI)),
                () -> assertFalse(Q1.isOwner(UserTest.SANJIGI))
        );
    }

    @Test
    void question_동등성_테스트() {
        assertAll(
                () -> assertEquals(
                        new Question(1L, "title", "contents")
                                .writeBy(UserTest.JAVAJIGI),
                        new Question(1L, "title2", "content2")
                                .writeBy(UserTest.JAVAJIGI)
                ),
                () -> assertNotEquals(
                        new Question(1L, "title", "contents")
                                .writeBy(UserTest.JAVAJIGI),
                        new Question(1L, "title", "contents")
                                .writeBy(UserTest.SANJIGI)
                )
        );
    }
}
