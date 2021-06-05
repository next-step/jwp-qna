package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {

    private final User anotherUser = mock(User.class);
    private final User writer = mock(User.class);

    @DisplayName("Question 작성자와 다른 ID로 삭제하면 CannotDeleteException 이 발생하는지 테스트")
    @Test
    void given_UserNotOwnQuestion_when_DeleteQuestion_then_ThrowCannotDeleteException() {
        // given
        final Question question = new Question("title", "contents", writer);
        question.addAnswer(new Answer(writer, question, "contents"));
        question.addAnswer(new Answer(writer, question, "contents"));

        // when
        final Throwable actualThrowable = catchThrowable(() -> question.delete(anotherUser));

        // then
        assertAll(
            () -> assertThat(actualThrowable).isInstanceOf(CannotDeleteException.class)
        );
    }

    @DisplayName("작성자가 2 명인 Answer 를 가지고 있는 Question 을 삭제하면 CannotDeleteException 이 발생하는지 테스트")
    @Test
    void given_QuestionThatHaveTwoWriter_when_DeleteQuestion_then_ThrowCannotDeleteException() {
        // given
        final Question question = new Question("title", "contents", writer);
        question.addAnswer(new Answer(writer, question, "contents"));
        question.addAnswer(new Answer(anotherUser, question, "contents"));

        // when
        final Throwable actualThrowable = catchThrowable(() -> question.delete(writer));

        // then
        assertAll(
            () -> assertThat(actualThrowable).isInstanceOf(CannotDeleteException.class)
        );
    }

    @DisplayName("Quesiton 과 Answer 를 모두 작성한 사용자가 Question 을 삭제하면 DeleteHistory 리스트가 반환되는지 테스트")
    @Test
    void given_WriterWriteQuestionAndAnswer_when_DeleteQuestion_then_ReturnDeleteHistories() throws
        CannotDeleteException {
        // given
        final Question question = new Question("title", "contents", writer);
        final Answer answer1 = new Answer(writer, question, "contents");
        final Answer answer2 = new Answer(writer, question, "contents");
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        final DeleteHistories expected = new DeleteHistories(expectedDeleteHistories(question, answer1, answer2));

        // when
        final DeleteHistories actual = question.delete(writer);

        // then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(3),
            () -> assertThat(actual).isEqualTo(expected)
        );
    }

    private List<DeleteHistory> expectedDeleteHistories(final Question question, final Answer answer1,
        final Answer answer2) {
        return Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, answer1.getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, answer2.getId(), writer, LocalDateTime.now()));
    }
}
