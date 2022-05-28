package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("name, email 업데이트 테스트")
    void update() {
        //given
        User oldJavajigi = new User(1L, "javagiji", "password", "name2", "javajigi@slipp.net");

        //when
        oldJavajigi.update(oldJavajigi, JAVAJIGI);

        //then
        assertAll(
                () -> assertThat(oldJavajigi.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(oldJavajigi.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("userId 불일치 시 업데이트 실패 테스트")
    void update_fail_userId() {
        //given
        User oldJavajigi = new User(1L, "javagiji", "password", "name2", "javajigi@slipp.net");
        User oldSanjigi = new User(2L, "sanjigi", "password", "name2", "sanjigi@slipp.net");

        //then
        assertThatThrownBy(() -> {
            oldJavajigi.update(oldSanjigi, JAVAJIGI);
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("password 불일치 시 업데이트 실패 테스트")
    void update_fail_password() {
        //given
        User oldJavajigi = new User(1L, "javagiji", "password2", "name2", "javajigi@slipp.net");

        //then
        assertThatThrownBy(() -> {
            oldJavajigi.update(oldJavajigi, JAVAJIGI);
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("게스트유저 여부 확인 테스트")
    void guestUser() {
        //given
        User guestUser = User.GUEST_USER;

        //then
        assertAll(
                () -> assertThat(JAVAJIGI.isGuestUser()).isFalse(),
                () -> assertThat(guestUser.isGuestUser()).isTrue()
        );
    }
}
