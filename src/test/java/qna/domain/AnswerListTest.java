package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswerListTest {

    @Test
    public void deletedSuccess() throws Exception {
        AnswerList answerList = new AnswerList();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer");
        answerList.add(answer);
        DeleteHistoryList deleteHistoryList = new DeleteHistoryList();
        answerList.deleted(UserTest.JAVAJIGI, deleteHistoryList);

        assertAll(() -> {
            assertThat(deleteHistoryList.size()).isEqualTo(1);
            assertThat(deleteHistoryList.getDeleteHistories().get(0).equals(answer));
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
            answerList.deleted(UserTest.SANJIGI, deleteHistoryList);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(throwingCallable);
    }

}