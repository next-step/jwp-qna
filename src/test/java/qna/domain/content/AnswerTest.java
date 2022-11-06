package qna.domain.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("답변 클래스 테스트")
public class AnswerTest {
    private static final Answer A1 =
            new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    private static final Answer A2 =
            new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void answer_객체를_생성시_user가_null이면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            new Answer(null, QuestionTest.Q1, "contents test");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void answer_객체를_생성시_question이_null이면_NotFoundException_발생() {
        assertThatThrownBy(() -> {
            new Answer(UserTest.JAVAJIGI, null, "contents test");
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void answer_객체_생성() {
        Question question = new Question(UserTest.JAVAJIGI, "title", "contents");
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");

        assertTrue(question.hasAnswer(answer));
    }

    @Test
    void update시_로그인한_사용자가_답변을_작성한_사용자와_다르면_UnAuthorizedException_발생() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents test");

        assertThatThrownBy(() -> {
            answer.update(UserTest.SANJIGI, "change contents");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 해당_유저가_작성한_답변인지_확인() {
        assertAll(
                () -> assertTrue(A1.isOwner(UserTest.JAVAJIGI)),
                () -> assertFalse(A1.isOwner(UserTest.SANJIGI))
        );
    }

    @Test
    void answer_동등성_테스트() {
        assertAll(
                () -> assertEquals(
                    new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents"),
                    new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents2")
                ),
                () -> assertNotEquals(
                        new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents"),
                        new Answer(1L, UserTest.SANJIGI, QuestionTest.Q1, "contents")
                )
        );
    }
}
