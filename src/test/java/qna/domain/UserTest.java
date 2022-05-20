package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("Answer 도메인 생성")
    @Test
    void test_new() {
        //given & when
        User newUser = new User(3L, "new", "password", "name", "new@slipp.net");
        //then
        assertThat(newUser).isNotNull();
    }

    @DisplayName("name, email 업데이트 후 변경 확인")
    @Test
    void test_update_equalsNameAndEmail() {
        //given
        JAVAJIGI.update(JAVAJIGI, SANJIGI);
        //when & then
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isTrue();
    }

    @DisplayName("업데이트 시 로그인 사용자가 동일하지 않을 경우 UnAuthorizedException")
    @Test
    void test_not_equals_login_user() {
        //given & when & then
        assertThatThrownBy(() -> JAVAJIGI.update(SANJIGI, SANJIGI))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("업데이트 시 타겟 사용자와 비밀번호가 동일하지 않을 경우 UnAuthorizedException")
    @Test
    void test_not_equals_password_target_user() {
        //given & when & then
        User target = new User(4L, "target", "targetPassword", "targetName", "target@slipp.net");
        assertThatThrownBy(() -> JAVAJIGI.update(JAVAJIGI, target))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("GuestUser 확인")
    @Test
    void test_guest_user() {
        //given
        User guestUser = User.GUEST_USER;
        //when & then
        assertAll(
                () -> assertThat(guestUser.isGuestUser()).isTrue(),
                () -> assertThat(JAVAJIGI.isGuestUser()).isFalse()
        );
    }
}
