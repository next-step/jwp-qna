package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("name, email 업데이트 후 변경 확인")
    @Test
    void test_update_equalsNameAndEmail() {
        //given
        JAVAJIGI.update(JAVAJIGI, SANJIGI);
        //when & then
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isTrue();
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
