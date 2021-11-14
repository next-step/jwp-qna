package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.Message;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Test
    @DisplayName("질문자가 답변 없는 질문 삭제")
    void delete_not_having_answer() throws CannotDeleteException {
        final Question question = Fixture.question("writer.id");
        question.delete(question.getWriter());
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자가 답변한 질문 삭제")
    void delete_answered_by_questioner() throws CannotDeleteException {
        final Question question = Fixture.question("writer.id");
        final User questioner = question.getWriter();
        question.addAnswer(Fixture.answer(question, questioner.getUserId()));
        question.delete(questioner);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자가 아닌 유저가 질문 삭제시 실패")
    void delete_without_ownership() {
        final Question question = Fixture.question("writer.id");
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> question.delete(Fixture.user("other.id")))
            .withMessage(Message.CAN_NOT_DELETE_QUESTION_WITHOUT_OWNERSHIP.getContent());
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 질문자가 질문 삭제시 (답변 삭제 실패로 인해) 실패")
    void delete_having_answer_written_by_other() {
        final Question question = Fixture.question("writer.id");
        question.addAnswer(Fixture.answer(question, "other.id"));
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> question.delete(question.getWriter()))
            .withMessage(Message.CAN_NOT_DELETE_QUESTION_HAVING_ANSWER_WRITTEN_BY_OTHER.getContent());
    }
}
