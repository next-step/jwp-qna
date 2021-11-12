package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.ErrorMessages;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", UserTest.SANJIGI);


    public Question question1;
    public Question question2;

    @BeforeEach
    public void setUp() {
        question1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
        question2 = new Question("title2", "contents2", UserTest.SANJIGI);
    }

    @Test
    @DisplayName("삭제 리스트 생성 성공")
    public void getDeleteHistorySuccess() throws Exception {
        Question question = question1;
        Answer answer = AnswerTest.A1;
        answer.writerBy(UserTest.JAVAJIGI);
        answer.toQuestion(question);

        DeleteHistoryList deleteHistoryList = question.deletedAndAnswers(UserTest.JAVAJIGI);

        assertThat(deleteHistoryList.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("답볌없는 질문 삭제히스토리 리스트 생성 성공")
    public void getDeleteHistoryNoAnswerSuccess() throws Exception {
        Question question = question1;

        DeleteHistoryList deleteHistoryList = question.deletedAndAnswers(UserTest.JAVAJIGI);

        assertThat(deleteHistoryList.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("다른 사용자 질문 삭제 실패")
    public void getDeleteHistoryFailOtherUserQuestion() {
        Question question = question1;
        Answer answer1 = AnswerTest.A1;
        answer1.writerBy(UserTest.JAVAJIGI);
        answer1.toQuestion(question);

        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
            question.deletedAndAnswers(UserTest.SANJIGI);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(throwingCallable)
            .withMessage(ErrorMessages.OTHER_USER_CANNOT_DELETE.getValues());

    }

    @Test
    @DisplayName("다른 사용자 답변 삭제 실패")
    public void getDeleteHistoryFailOtherUserAnswer() {
        Question question = question1;
        Answer answer1 = AnswerTest.A1;
        answer1.writerBy(UserTest.JAVAJIGI);
        answer1.toQuestion(question);
        Answer answer2 = AnswerTest.A2;
        answer2.writerBy(UserTest.SANJIGI);
        answer2.toQuestion(question);

        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
            question.deletedAndAnswers(UserTest.JAVAJIGI);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(throwingCallable)
            .withMessage(ErrorMessages.ANSWER_OTHER_USER_CANNOT_DELETE.getValues());

    }
}
