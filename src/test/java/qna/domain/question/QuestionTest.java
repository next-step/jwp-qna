package qna.domain.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.QnAExceptionMessage;
import qna.domain.answer.AnswerTest;
import qna.domain.user.UserTest;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QnAExceptionMessage.NO_AUTH_ANSWER;
import static qna.domain.QnAExceptionMessage.NO_AUTH_QUESTION;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);


    @Test
    @DisplayName("소유자와 하위 답글을 작성한 유저가 삭제를 호출했을 경우 테스트")
    void checkOwner() {
        // given
        Question question = new Question().writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A1);

        // when
        assertThatCode(() -> question.delete(UserTest.JAVAJIGI))
            .doesNotThrowAnyException(); // then
    }

    @Test
    @DisplayName("하위 답글을 작성한 유저가 다를 경우 테스트")
    void checkOwnerWithNotSameOwnerToAnswer() {
        // given
        Question question = new Question().writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A2);

        // when
        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining(NO_AUTH_ANSWER.message()); // then
    }

    @Test
    @DisplayName("작성한 유저가 아닌 유저가 삭제 테스트")
    void checkOwnerWithNotSameOwnerToQuestion() {
        // given
        Question question = new Question().writeBy(UserTest.JAVAJIGI);
        question.addAnswer(AnswerTest.A1);

        // when
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining(NO_AUTH_QUESTION.message()); // then
    }
}
