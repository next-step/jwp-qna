package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class DeleteHistoryTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        DeleteHistory actual = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        DeleteHistory actual = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    @DisplayName("Question 삭제 시 삭제이력 확인")
    void questionDeleteHistory() throws CannotDeleteException {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        DeleteHistories result = question.delete(UserTest.JAVAJIGI);

        List<DeleteHistory> histories = result.getHistories();

        assertAll(
                () -> assertThat(histories).hasSize(1),
                () -> assertThat(histories.get(0)).isEqualTo(
                        DeleteHistory.ofQuestion(question.getId(), UserTest.JAVAJIGI)
                )
        );
    }

    @Test
    @DisplayName("Answer 삭제 시 삭제이력 확인")
    void AnswerDeleteHistory() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, question, "contents");

        DeleteHistory deleteHistory = answer.delete();

        Assertions.assertThat(deleteHistory).isEqualTo(DeleteHistory.ofAnswer(answer.getId(), UserTest.JAVAJIGI));
    }
}