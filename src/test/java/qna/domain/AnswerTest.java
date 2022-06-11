package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 작성 시, 작성자가 null이면 오류가 발생한다.")
    void checkExceptionCreateAnswerWithWriterIsNull() {
        assertThatThrownBy(() -> new Answer(1L, null, QuestionTest.Q1, "wow~")).isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 작성 시, 질문이 null이면 오류가 발생한다.")
    void checkExceptionCreateAnswerWithQuestionIsNull() {
        assertThatThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, null, "wow~!")).isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("글의 작성자(주인)을 확인한다.")
    void checkOwner() {
        assertAll(() -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue());
        assertAll(() -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue());
    }

    @Test
    @DisplayName("질문 등록할 수 있다.")
    void checkToQuestion() {
        assertAll(() -> {
            A1.setQuestion(QuestionTest.Q2);
            assertThat(A1.getQuestion().getId()).isEqualTo(QuestionTest.Q2.getId());
        });
        assertAll(() -> {
            A2.setQuestion(QuestionTest.Q2);
            assertThat(A2.getQuestion().getId()).isEqualTo(QuestionTest.Q2.getId());
        });
    }

    @Test
    @DisplayName("답변삭제 시 작성자가 아닐 경우 오류가 발생한다.")
    void checkExceptionUserId() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI)).isExactlyInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> A2.delete(UserTest.JAVAJIGI)).isExactlyInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변 작성자와 로그인 유저가 일치할 경우 답변이 삭제된다.")
    void checkDeleteAnswer() {
        assertAll(() -> {
            A1.delete(A1.getUser());
            assertThat(A1.isDeleted()).isTrue();
        });
        assertAll(() -> {
            A2.delete(A2.getUser());
            assertThat(A2.isDeleted()).isTrue();
        });
    }

    @Test
    @DisplayName("답변 삭제 히스토리를 생성할 수 있다.")
    void checkCreateDeleteHistory() {
        assertAll(() -> {
            DeleteHistory deleteHistory = A1.createDeleteHistory();
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
            assertThat(deleteHistory.getContentId()).isEqualTo(A1.getId());
        });
        assertAll(() -> {
            DeleteHistory deleteHistory = A2.createDeleteHistory();
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
            assertThat(deleteHistory.getContentId()).isEqualTo(A2.getId());
        });
    }
}
