package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("유저 정보 변경시 유저의 아이디와 로그인 유저의 아이디와 같지 않으면 UnAuthorizedException 발생")
    @Test
    void update_wrongUserId_exception() {

        User user = new User("user", "password", "name", "name@email.com");
        User loginUser = new User("userFail", "password", "name", "name@email.com");
        User target = new User("user", "password", "nameNew", "nameNew@email.com");

        assertThatThrownBy(() -> user.update(loginUser, target)).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("유저 정보 변경시 유저의 비밀번호와 타겟 유저의 비밀번호와 같지 않으면 UnAuthorizedException 발생")
    @Test
    void update_wrongPassword_exception() {
        User user = new User("user", "password", "name", "name@email.com");
        User loginUser = new User("user", "password", "name", "name@email.com");
        User target = new User("user", "passwordFail", "nameNew", "nameNew@email.com");

        assertThatThrownBy(() -> user.update(loginUser, target)).isInstanceOf(UnAuthorizedException.class);

    }

    @DisplayName("로그인 유저의 아이디와 타겟 유저의 비밀번호가 유저 아이디 비밀번호와 동일하면 이름과 이메일 변경이 가능하다")
    @Test
    void update_test() {
        // given
        User user = new User("user", "password", "name", "name@email.com");
        User loginUser = new User("user", "password", "name", "name@email.com");
        User target = new User("user", "password", "nameNew", "nameNew@email.com");
        // when
        user.update(loginUser, target);
        // then
        assertAll(
                () -> assertEquals(user.getName(), target.getName()),
                () -> assertEquals(user.getEmail(), target.getEmail())
        );
    }

    @DisplayName("유저의 이름과 이메일이 같은지 확인한다")
    @Test
    void equalsNameAndEmail_test() {

        User user = new User("user", "password", "name", "name@email.com");
        User target = new User("target", "targetPassword", "name", "name@email.com");

        assertTrue(user.equalsNameAndEmail(target));
    }

    @DisplayName("게스트 유저를 확인할 수 있다")
    @Test
    void isGuestUser_test() {
        User guest = User.GUEST_USER;
        assertAll(
                () -> assertFalse(UserTest.JAVAJIGI.isGuestUser()),
                () -> assertTrue(guest.isGuestUser())
        );
    }
}
