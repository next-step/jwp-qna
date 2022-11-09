package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("작성자가 존재하지 않을 경우 에러가 발생되는지 확인")
    @Test
    void vaildateWriterException() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "contents"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("질문이 존재하지 않을 경우 에러가 발생되는지 확인")
    @Test
    void vaildateQuestionException() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "contents"))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("두 유저 값이 동일한지 확인")
    @Test
    void isOwner() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");

        assertAll(
            () -> assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(answer.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @DisplayName("삭제가 성공하는지 확인")
    @Test
    void delete() {
        DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);

        assertAll(
            () -> assertThat(A1.isDeleted()).isTrue(),
            () -> assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), LocalDateTime.now()))
        );
    }

    @DisplayName("다른 사람이 쓴 답변인 경우 에러가 발생되는지 확인")
    @Test
    void vaildateOwnerException() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }
}
