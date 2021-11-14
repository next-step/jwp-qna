package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.Message;

public class AnswerTest {

    @Test
    @DisplayName("작성자가 답변 삭제")
    void delete() throws CannotDeleteException {
        final Answer answer = Fixture.answer("writer.id");
        answer.delete(answer.getWriter());
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자가 아닌 유저가 답변 삭제시 실패")
    void delete_without_ownership() {
        final User loginUser = Fixture.user("not.writer.id");
        final Answer answer = Fixture.answer("writer.id");
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answer.delete(loginUser))
            .withMessage(Message.CAN_NOT_DELETE_ANSWER_WITHOUT_OWNERSHIP.getContent());
    }
}
