package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {

    @Test
    @DisplayName("본인이 쓴 댓글은 삭제가 가능하다")
    void deleted() throws CannotDeleteException {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThat(answer.isDeleted()).isFalse();
        DeleteHistory delHistory = answer.delete(UserTest.JAVAJIGI);
        assertThat(answer.isDeleted()).isTrue();
        assertThat(delHistory).isNotNull();
    }

    @Test
    @DisplayName("다른 유저가 쓴 댓글을 삭제 시도시 예외가 발생한다")
    void deletedException() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThatThrownBy(() -> answer.delete(UserTest.IU)).isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 삭제된 답변을 다시 삭제할 경우 예외가 발생한다")
    void deletedAgainException() throws CannotDeleteException {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer.delete(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> answer.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class)
                .hasMessage("이미 삭제된 답변입니다.");

    }
}
