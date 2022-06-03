package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1, "Answers Contents2");

    @Test
    @DisplayName("답변의 작성자가 null일 경우 예외를 발생시킨다.")
    void UnAuthorizedException() {
        assertThatThrownBy(() -> new Answer(3L, null, Q1, "answer contents"))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변에 해당하는 질문이 null일 경우 예외를 발생시킨다.")
    void NotFoundException() {
        assertThatThrownBy(() -> new Answer(3L, UserTest.JAVAJIGI, null, "answer contents"))
                .isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변의 작성자가 맞는지 확인한다.")
    void isOwner() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI))
                .isTrue();
        assertThat(A2.isOwner(UserTest.SANJIGI))
                .isTrue();
    }

    @Test
    @DisplayName("입력받은 질문에 답변을 추가한다.")
    void toQuestion() {
        Question question = new Question(10L, "Test Question", "Test Question Contents");
        A1.toQuestion(question);

        assertThat(A1.getQuestion())
                .isEqualTo(question)
                .hasFieldOrPropertyWithValue("id", 10L);
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void test() throws CannotDeleteException {
        DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);

        assertThat(A1.isDeleted())
                .isTrue();
        assertThat(deleteHistory)
                .isNotNull();
    }

    @Test
    @DisplayName("다른 사람이 쓴 답변인 경우 예외를 발생시킨다.")
    void delete_wrong_writer() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isExactlyInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 삭제한 답변이 있으면 예외를 발생시킨다.")
    void delete_already_deleted() {
        A1.setDeleted(true);

        assertThatThrownBy(() -> A1.delete(JAVAJIGI))
                .isExactlyInstanceOf(CannotDeleteException.class)
                .hasMessage("이미 삭제된 답변 입니다.");
    }
}
