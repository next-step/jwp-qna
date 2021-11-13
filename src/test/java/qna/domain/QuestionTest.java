package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.exception.ExceptionMessage.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exception.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우에만 삭제가 가능하다")
    @Test
    void validateQuestionOwner() {
        assertDoesNotThrow(() -> Q1.validateQuestionOwner(UserTest.JAVAJIGI));
        assertThatThrownBy(() -> Q1.validateQuestionOwner(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage(CANNOT_DELETE_QUESTION_MESSAGE.getMessage());
    }
}
