package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("생성자 테스트")
    void create_user() {
        assertThatCode(() -> new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("user 이름 이메일 업데이트")
    void update_user() {
        User loginUser = JAVAJIGI;
        User targetUser = new User(1L, "seungki", "password", "seungki", "test@email");
        loginUser.update(loginUser, targetUser);
        assertThat(loginUser.getEmail()).isEqualTo(targetUser.getEmail());
        assertThat(loginUser.getName()).isEqualTo(targetUser.getName());
    }

    @Test
    @DisplayName("user 이름 이메일 업데이트 테스트_id불일치")
    void update_user_exception_unauthorized_exception() {
        User targetUser = new User(2L, "seungki", "password", "seungki", "test@email");
        assertThatThrownBy(() -> JAVAJIGI.update(targetUser, targetUser)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("user 이름 이메일 업데이트 테스트_password불일치")
    void update_user_exception_unauthorized_exception_password() {
        User targetUser = new User(2L, "seungki", "password1", "seungki", "test@email");
        assertThatThrownBy(() -> JAVAJIGI.update(JAVAJIGI, targetUser)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("user 패스워드 일치 테스트")
    void password_correct() {
        String password = JAVAJIGI.getPassword();
        assertThat(JAVAJIGI.matchPassword(password)).isTrue();
    }

    @Test
    @DisplayName("user 패스워드 불일치 테스트")
    void password_incorrect() {
        String password = "incorrect password";
        assertThat(JAVAJIGI.matchPassword(password)).isFalse();
    }

    @Test
    @DisplayName("user 이름 이메일 불 일치 테스트")
    void name_password_incorrect() {
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isFalse();
    }

    @Test
    @DisplayName("user 이름 이메일 일치 테스트")
    void name_password_correct() {
        assertThat(JAVAJIGI.equalsNameAndEmail(JAVAJIGI)).isTrue();
    }
}
