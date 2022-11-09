package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 테스트")
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("회원 업데이트 확인")
    void user_update() {
        User user = JAVAJIGI;

        User updateUser = new User("changeId", "password", "changeName", "changeEmail");
        user.update(user, updateUser);

        assertThat(user.equalsNameAndEmail(updateUser)).isTrue();
    }

    @Test
    @DisplayName("손님 계정 확인")
    void isGuestUser() {
        User guestUser = User.GUEST_USER;

        boolean isGuest = guestUser.isGuestUser();

        assertThat(isGuest).isTrue();
    }
}
