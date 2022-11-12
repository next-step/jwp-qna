package qna.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");

    @Test
    @DisplayName("질문자와 답변자가 다른 경우")
    void not_equal_answer_user_and_writer_user_delete_test() {
        User expected = UserTest.SANJIGI;

        assertThatThrownBy(() -> A1.delete(expected))
                .isInstanceOf(CannotDeleteException.class).hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("삭제가 성공해서, 삭제 상태값이 변경되는 테스트")
    void success_answer_delete_test() throws CannotDeleteException {
        Answer expected = AnswerTest.A1;

        expected.delete(UserTest.JAVAJIGI);

        assertThat(expected.getDeleted()).isTrue();
    }
}
