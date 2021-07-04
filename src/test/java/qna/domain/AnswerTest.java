package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("답변 개별 삭제 : 로그인 사용자와 답변 작성자가 같을 경우 삭제")
    @Test
    void deleteBySameUser() throws CannotDeleteException {
        User loginUser = UserTest.JAVAJIGI;
        A1.delete(loginUser);
        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("답변 개별 삭제 : 로그인 사용자와 답변 작성자가 다를 경우 예외 발생")
    @Test
    void deleteByWrongUser() {
        User loginUser = UserTest.SANJIGI;
        assertThatThrownBy(() -> A1.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

}
