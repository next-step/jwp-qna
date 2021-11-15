package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.ErrorMessages;

class AnswerListTest {

    @Test
    @DisplayName("질문 리스트 생성 성공")
    public void createAnswerList() {
        Answer answer = AnswerTest.A1;

        AnswerList answerList = new AnswerList();
        answerList.add(answer);

        assertThat(answerList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문 리스트 삭제 플래그 true 성공")
    public void deletedSuccess() throws Exception {
        AnswerList answerList = new AnswerList();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer");
        answerList.add(answer);

        assertThat(answer.isDeleted()).isFalse();

        DeleteHistoryList deleteHistoryList = new DeleteHistoryList();
        answerList.deleteAnswers(UserTest.JAVAJIGI, deleteHistoryList);

        assertAll(() -> {
            assertThat(deleteHistoryList.size()).isEqualTo(1);
            assertThat(answer.isDeleted()).isTrue();
        });
    }

    @Test
    public void deletedFail() {
        AnswerList answerList = new AnswerList();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer");
        answerList.add(answer);
        DeleteHistoryList deleteHistoryList = new DeleteHistoryList();

        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
            answerList.deleteAnswers(UserTest.SANJIGI, deleteHistoryList);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(throwingCallable)
            .withMessage(ErrorMessages.ANSWER_OTHER_USER_CANNOT_DELETE.getValues());
    }

}