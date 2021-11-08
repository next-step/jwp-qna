package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");


    @Test
    @DisplayName("답변 생성 시 사용자 없는 경우 UnAuthorizedException 반환")
    public void createEmptyUserUnAuthorizedExceptionFail() {
        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
            new Answer(null, QuestionTest.Q1, "failed contents1");

        //then
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(throwingCallable);
    }

    @Test
    @DisplayName("답변 생성 시 질문 없는 경우 NotFoundException 반환")
    public void createEmptyQuestionNotFoundExceptionFail() {
        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
            new Answer(UserTest.JAVAJIGI, null, "failed contents1");

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(throwingCallable);
    }

}
