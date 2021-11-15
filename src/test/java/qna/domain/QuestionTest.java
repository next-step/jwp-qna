package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.ErrorMessage;
import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Test
    @DisplayName("질문자가 답변 없는 질문 삭제")
    void delete_not_having_answer() throws CannotDeleteException {
        final Question question = QuestionFixture.식별자가_userId인_유저가_작성한_질문("writer.id");
        final List<DeleteHistory> deleteHistories = question.delete(question.getWriter());
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> {
                assertThat(deleteHistories).isNotNull();
                assertThat(deleteHistories.size()).isEqualTo(1);
            }
        );
    }

    @Test
    @DisplayName("질문자가 답변한 질문 삭제")
    void delete_answered_by_questioner() throws CannotDeleteException {
        final Question question = QuestionFixture.식별자가_userId인_유저가_작성한_질문("writer.id");
        final User questioner = question.getWriter();
        AnswerFixture.질문과_userId로_식별되는_작성자를_갖는_답변(question, questioner.getUserId());

        final List<DeleteHistory> deleteHistories = question.delete(questioner);
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> {
                assertThat(deleteHistories).isNotNull();
                assertThat(deleteHistories.size()).isEqualTo(2);
            }
        );
    }

    @Test
    @DisplayName("질문자가 아닌 유저가 질문 삭제시 실패")
    void delete_without_ownership() {
        final Question question = QuestionFixture.식별자가_userId인_유저가_작성한_질문("writer.id");
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> question.delete(UserFixture.식별자가_userId인_유저("other.id")))
            .withMessage(ErrorMessage.CAN_NOT_DELETE_QUESTION_WITHOUT_OWNERSHIP.getContent());
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 질문자가 질문 삭제시 (답변 삭제 실패로 인해) 실패")
    void delete_having_answer_written_by_other() {
        final Question question = QuestionFixture.식별자가_userId인_유저가_작성한_질문("writer.id");
        question.addAnswer(
            AnswerFixture.질문과_userId로_식별되는_작성자를_갖는_답변(question, "other.id")
        );
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> question.delete(question.getWriter()))
            .withMessage(ErrorMessage.CAN_NOT_DELETE_QUESTION_HAVING_ANSWER_WRITTEN_BY_OTHER.getContent());
    }
}
