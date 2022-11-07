package qna.domain.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.history.DeleteHistory;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.domain.UserTest;
import qna.exception.message.AnswerExceptionCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("답변 클래스 테스트")
public class AnswerTest {

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, UserTest.JAVAJIGI, "title", "contents");
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "contents");
    }

    @Test
    void answer_객체를_생성시_user가_null이면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            new Answer(null, QuestionTest.Q1, "contents test");
        }).isInstanceOf(UnAuthorizedException.class)
                .hasMessage(AnswerExceptionCode.REQUIRED_WRITER.getMessage());
    }

    @Test
    void answer_객체를_생성시_question이_null이면_NotFoundException_발생() {
        assertThatThrownBy(() -> {
            new Answer(UserTest.JAVAJIGI, null, "contents test");
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(AnswerExceptionCode.REQUIRED_QUESTION.getMessage());
    }

    @Test
    void answer_객체_생성() {
        Question question = new Question(UserTest.JAVAJIGI, "title", "contents");
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");

        assertTrue(question.hasAnswer(answer));
    }

    @Test
    void update시_로그인한_사용자가_답변자와_다르면_UnAuthorizedException_발생() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents test");

        assertThatThrownBy(() -> {
            answer.update(UserTest.SANJIGI, "change contents");
        }).isInstanceOf(UnAuthorizedException.class)
                .hasMessage(AnswerExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
    }

    @Test
    void delete시_로그인한_사용자가_답변자와_다르면_CannotDeleteException_발생() {
        assertThatThrownBy(() -> {
           answer.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessage(AnswerExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
    }

    @Test
    void delete시_이미_삭제된_답변이면_CannotDeleteException_발생() {
        answer.delete(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> {
            answer.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessage(AnswerExceptionCode.ALREADY_DELETED.getMessage());
    }

    @Test
    void delete시_답변이_삭제된다() {
        DeleteHistory deleteHistory = answer.delete(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(deleteHistory).satisfies((history) -> {
                        assertEquals(ContentType.ANSWER, deleteHistory.getContentType());
                        assertEquals(answer.getId(), deleteHistory.getContentId());
                }),
                () -> assertTrue(answer.isDeleted())
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
