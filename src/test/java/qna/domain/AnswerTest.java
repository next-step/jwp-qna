package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {

    private final User writer = new User("id", "password", "name", "email");
    private final User anotherUser = new User("id2", "password2", "name2", "email2");

    @DisplayName("Answer 를 작성하지 않은 사용자로 Answer 에 삭제 메시지를 보내면 CannotDeleteException 이 발생하는지 테스트")
    @Test
    void given_UserNotOwnAnswer_when_DeleteAnswer_then_ThrowCannotDeleteException() {
        // given
        final Answer answer = new Answer(writer, new Question("title", "contents", writer), "contents");

        // when
        final Throwable throwable = catchThrowable(() -> answer.delete(anotherUser));

        // then
        assertThat(throwable).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("Answer 를 작성한 사용자로 Answer 에 삭제 메시지를 보내면 DeleteHistory 를 반환하는지 테스트")
    @Test
    void given_UserOwnAnswer_when_DeleteAnswer_then_DeleteHistory() throws CannotDeleteException {
        // given
        final Answer answer = new Answer(writer, new Question("title", "contents", writer), "contents");
        final DeleteHistory expected = new DeleteHistory(ANSWER, answer.getId(), writer, LocalDateTime.now());

        // when
        final DeleteHistory actual = answer.delete(writer);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
