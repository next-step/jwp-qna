package qna.domain.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("질문 클래스 테스트")
public class QuestionTest {
    public static final Question Q1 = new Question(UserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(UserTest.SANJIGI, "title2", "contents2");

    @Test
    void Question_객체_생성시_writer가_null이면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            new Question(null, "title", "contents");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void Question_객체_생성시_title이_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new Question(UserTest.JAVAJIGI, null, "contents");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 해당_유저가_작성한_질문인지_확인() {
        assertAll(
                () -> assertTrue(Q1.isOwner(UserTest.JAVAJIGI)),
                () -> assertFalse(Q1.isOwner(UserTest.SANJIGI))
        );
    }

    @Test
    void update시_로그인한_사용자와_질문의_작성자가_다르면_UnAuthorizedException_발생() {
        Question question = new Question(UserTest.JAVAJIGI, "title", "contents");
        assertThatThrownBy(() -> {
            question.update(UserTest.SANJIGI, "title2", "contents2");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void update시_title이_null이면_IllegalArgumentException_발생() {
        Question question = new Question(UserTest.JAVAJIGI, "title", "contents");
        assertThatThrownBy(() -> {
            question.update(UserTest.JAVAJIGI, null, "contents2");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void question_동등성_테스트() {
        assertAll(
                () -> assertEquals(
                        new Question(1L, UserTest.JAVAJIGI, "title", "contents"),
                        new Question(1L, UserTest.JAVAJIGI, "title2", "content2")
                ),
                () -> assertNotEquals(
                        new Question(1L, UserTest.JAVAJIGI, "title", "contents"),
                        new Question(1L, UserTest.SANJIGI, "title", "contents")
                )
        );
    }
}
