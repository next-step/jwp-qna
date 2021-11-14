package qna.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 질문 삭제")
    @Test
    void deleteSuccess() throws CannotDeleteException {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        DeleteHistory deleteHistory = answer.delete(UserTest.JAVAJIGI);

        assertThat(answer.isDeleted()).isTrue();
        assertThat(deleteHistory.getDeletedBy()).isEqualTo(UserTest.JAVAJIGI);
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다르면 예외 발생")
    @Test
    void deleteFail() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        ThrowableAssert.ThrowingCallable throwingCallable = () -> answer.delete(UserTest.SANJIGI);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
