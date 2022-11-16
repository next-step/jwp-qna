package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer answerByJavajigi = new Answer(UserTest.JAVAJIGI, QuestionTest.questionByJavajigi, "Answers Contents1");
    public static final Answer answerBySanjigi = new Answer(UserTest.SANJIGI, QuestionTest.questionByJavajigi, "Answers Contents2");

    @Test
    @DisplayName("로그인한 사용자의 것이 아닌 질문글을 삭제할 때 validate 오류")
    public void validateDelete() {
        assertThatThrownBy(() -> {
            answerByJavajigi.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("삭제 성공")
    public void delete() throws CannotDeleteException {
        DeleteHistory deleteHistory = answerBySanjigi.delete(UserTest.SANJIGI);
        assertThat(deleteHistory).isNotNull();
    }
}
