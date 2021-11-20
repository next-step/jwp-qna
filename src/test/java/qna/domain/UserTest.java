package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


    @Test
    @DisplayName("유저정보 업데이트(성공)")
    void update() {
        User userOld = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        User userNewInfo = new User(1L, "javajigi", "password", "changed", "changed@slipp.net");
        User userLogin = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        userOld.update(userLogin, userNewInfo);
        assertThat(userOld.getName()).isEqualTo("changed");
        assertThat(userOld.getEmail()).isEqualTo("changed@slipp.net");
    }

    @Test
    @DisplayName("유저정보 업데이트(실패)")
    void updateFail() {
        User userOld = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        User userNewInfo = new User(1L, "javajigi", "password", "changed", "changed@slipp.net");
        User userNewInfoInvalidPW = new User(1L, "javajigi", "invalid", "changed", "changed@slipp.net");
        User userLogin = new User(1L, "invalid", "password", "name", "javajigi@slipp.net");

        assertThatThrownBy(()->{
            userOld.update(userLogin, userNewInfo);
        }).isInstanceOf(UnAuthorizedException.class);

        assertThatThrownBy(()->{
            userOld.update(userLogin, userNewInfoInvalidPW);
        }).isInstanceOf(UnAuthorizedException.class);
    }
}
