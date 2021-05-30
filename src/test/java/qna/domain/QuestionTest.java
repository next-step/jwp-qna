package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.exception.BlankValidateException;
import qna.exception.UnAuthenticationException;
import qna.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("생성 메서드 유효성 예외 발생")
    @Test
    void createQuestion_exception() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");

        assertThatThrownBy(() -> Question.createQuestion(null, "testContents", writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("", "testContents", writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("testTile", null, writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("testTile", "", writer))
                .isInstanceOf(BlankValidateException.class);
        assertThatThrownBy(() -> Question.createQuestion("testTile", "testContents", null))
                .isInstanceOf(UnAuthenticationException.class);
    }
}
