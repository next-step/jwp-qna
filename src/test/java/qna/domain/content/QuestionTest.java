package qna.domain.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.history.DeleteHistory;
import qna.exception.CannotDeleteException;
import qna.exception.UnAuthorizedException;
import qna.domain.UserTest;
import qna.exception.message.AnswerExceptionCode;
import qna.exception.message.QuestionExceptionCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("질문 클래스 테스트")
public class QuestionTest {
    public static final Question Q1 = new Question(UserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(UserTest.SANJIGI, "title2", "contents2");

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, UserTest.JAVAJIGI, "title", "contents");
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "contents");
    }

    @Test
    void Question_객체_생성시_writer가_null이면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            new Question(null, "title", "contents");
        }).isInstanceOf(UnAuthorizedException.class)
                .hasMessage(QuestionExceptionCode.REQUIRED_WRITER.getMessage());
    }

    @Test
    void Question_객체_생성시_title이_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new Question(UserTest.JAVAJIGI, null, "contents");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionExceptionCode.REQUIRED_TITLE.getMessage());
    }

    @Test
    void update시_로그인한_사용자와_질문의_작성자가_다르면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            question.update(UserTest.SANJIGI, "title2", "contents2");
        }).isInstanceOf(UnAuthorizedException.class)
                .hasMessage(QuestionExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
    }

    @Test
    void update시_title이_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            question.update(UserTest.JAVAJIGI, null, "contents2");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionExceptionCode.REQUIRED_TITLE.getMessage());
    }

    @Test
    void delete시_로그인한_사용자와_질문자가_다르면_CannotDeleteException_발생() {
        assertThatThrownBy(() -> {
            Q1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessage(QuestionExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
    }

    @Test
    void delete시_이미_삭제된_질문인_경우_CannotDeleteException_발생() {
        question.delete(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> {
            question.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessage(QuestionExceptionCode.ALREADY_DELETED.getMessage());
    }

    @Test
    void delete시_질문자와_다른_답변자의_답변이_포함된_경우_CannotDeleteException_발생() {
        new Answer(1L, UserTest.SANJIGI, question, "contents");

        assertThatThrownBy(() -> {
            question.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessage(AnswerExceptionCode.NOT_MATCH_LOGIN_USER.getMessage());
    }

    @Test
    void delete시_이미_삭제된_답변은_DeleteHistory를_생성하지_않는다() {
        answer.delete(UserTest.JAVAJIGI);

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(1),
                () -> assertEquals(question.getId(), deleteHistories.get(0).getContentId()),
                () -> assertEquals(ContentType.QUESTION, deleteHistories.get(0).getContentType())
        );
    }

    @Test
    void delete시_질문을_삭제하면_답변도_삭제된다() {
        Answer answer2 = new Answer(2L, UserTest.JAVAJIGI, question, "contents");

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(3),
                () -> assertTrue(question.isDeleted() && answer.isDeleted()
                        && answer2.isDeleted())
        );
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
