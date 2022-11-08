package qna.domain.answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.question.Question;
import qna.domain.question.QuestionTest;
import qna.domain.question.factory.QuestionFactory;
import qna.domain.question.factory.QuestionFactoryImpl;
import qna.domain.user.User;
import qna.domain.user.UserTest;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 같으면 통과")
    void delete_success() {
        User loginUser = getUser(2L, "writer", "1111", "작성자", "writer@naver.com");
        User answerUser = getUser(2L, "writer", "1111", "작성자", "writer@naver.com");
        Answer answer = new Answer(answerUser, getQuestion("title", "content"), "");
        assertThatNoException().isThrownBy(() -> answer.delete(loginUser));
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 다르면 CannotDeleteException 발생")
    void delete_ex() {
        User loginUser = getUser(2L, "writer", "1111", "작성자", "writer@naver.com");
        User answerUser = getUser(1L, "writer", "1111", "작성자", "writer@naver.com");
        Answer answer = new Answer(answerUser, getQuestion("title", "content"), "");
        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> answer.delete(loginUser));
        assertThat(answer.isDeleted()).isFalse();
    }

    private User getUser(Long id, String userId, String password, String name, String email) {
        return new User(id, new UserId(userId), new Password(password), new Name(name), new Email(email));
    }

    private Question getQuestion(String title, String content) {
        QuestionFactory factory = new QuestionFactoryImpl();
        return factory.create(title, content)
                .writeBy(getUser(1L, "writer", "1111", "작성자", "writer@naver.com"));
    }
}
