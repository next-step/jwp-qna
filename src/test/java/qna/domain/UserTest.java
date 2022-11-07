package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(JAVAJIGI())
                .isEqualTo(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net")),
            () -> assertThat(JAVAJIGI())
                .isNotEqualTo(new User(2L, "javajigi", "password", "name", "javajigi@slipp.net"))
        );
    }

    @Test
    void 패스워드_일치_체크() {
        assertAll(
            () -> assertThat(JAVAJIGI().matchPassword("password")).isTrue(),
            () -> assertThat(JAVAJIGI().matchPassword("wrongPassword")).isFalse()
        );
    }

    @Test
    void 이름_이메일_일치_체크() {
        assertThat(JAVAJIGI().equalsNameAndEmail(JAVAJIGI())).isTrue();
        assertThat(JAVAJIGI().equalsNameAndEmail(SANJIGI())).isFalse();
    }

    @Test
    void 업데이트_로그인유저_userId_불일치_UnAuthorizedException() {
        User user = new User("wongUserId", "password", "name", "javajigi@slipp.net");
        User target = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> user.update(JAVAJIGI(), target))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 업데이트_타겟유저_password_불일치_UnAuthorizedException() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User target = new User("sanjigi", "wrongPassword", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> user.update(JAVAJIGI(), target))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 업데이트_성공() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User target = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        user.update(JAVAJIGI(), target);

        assertAll(
            () -> assertThat(user.getUserId()).isEqualTo("javajigi"),
            () -> assertThat(user.getPassword()).isEqualTo("password")
        );
    }
}
