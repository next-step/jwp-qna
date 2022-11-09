package qna.domain.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.question.title.Title;
import qna.domain.user.User;
import qna.domain.user.UserTest;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

public class QuestionTest {
    public static final Question Q1 = new Question(new Title("title1"), "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(new Title("title2"), "contents2").writeBy(UserTest.SANJIGI);


    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같으면 통과")
    void delete_success() {
        User loginUser = getUser(1L, "writer", "1111", "작성자", "writer@naver.com");
        Question question = getQuestion("title1", "content1");
        assertThatNoException().isThrownBy(() -> question.delete(loginUser));
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 다르면 CannotDeleteException 발생")
    void delete_ex() {
        User loginUser = getUser(2L, "writer", "1111", "작성자", "writer@naver.com");
        Question question = getQuestion("title1", "content1");
        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> question.delete(loginUser));
        assertThat(question.isDeleted()).isFalse();
    }

    private User getUser(Long id, String userId, String password, String name, String email) {
        return new User(id, new UserId(userId), new Password(password), new Name(name), new Email(email));
    }

    private Question getQuestion(String title, String content) {
        return new Question(new Title(title), content)
                .writeBy(getUser(1L, "writer", "1111", "작성자", "writer@naver.com"));
    }
}
