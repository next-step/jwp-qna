package qna.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void 유저_이름_이메일_업데이트() {
        User beforeUpdatedUser = new User("soob", "password", "name", "email@email.com");
        User afterUpdatedUser = new User("soob", "password", "name2", "email2@email.com");

        beforeUpdatedUser.update(beforeUpdatedUser, afterUpdatedUser);

        assertThat(beforeUpdatedUser.equalsNameAndEmail(afterUpdatedUser)).isTrue();
    }

    @Test
    void 게스트_유저_확인인() {
        User guestUser = User.GUEST_USER;
        assertAll(
                () -> assertThat(guestUser.isGuestUser()).isTrue(),
                () -> assertThat(JAVAJIGI.isGuestUser()).isFalse()
        );
    }
}
