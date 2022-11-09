package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

class UserAuthTest {
    @Test
    @DisplayName("userId가 null일 경우 IllegalArgumentException 예외 던지기")
    void create_user_auth_user_id_is_null_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> new UserAuth(null, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("password가 null일 경우 IllegalArgumentException 예외 던지기")
    void create_user_auth_password_is_null_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> new UserAuth("user", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("유저 정보 업데이트 유효성 검사 실패 (아이디)")
    void update_user_valid_user_id_throw_UnAuthorizedException() {
        UserAuth userAuth = new UserAuth("user", "password");
        UserAuth loginAuth = new UserAuth("login", "password");
        UserAuth targetAuth = new UserAuth("user", "password");
        assertThatThrownBy(() -> userAuth.validUpdate(loginAuth, targetAuth))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("유저 정보 업데이트 유효성 검사 실패 (패스워드)")
    void update_user_valid_password_throw_UnAuthorizedException() {
        UserAuth userAuth = new UserAuth("user", "password");
        UserAuth loginAuth = new UserAuth("user", "password");
        UserAuth targetAuth = new UserAuth("target", "password1");
        assertThatThrownBy(() -> userAuth.validUpdate(loginAuth, targetAuth))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
