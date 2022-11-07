package qna.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    @DisplayName("게스트 유저 확인")
    void is_guest_user() {
        User guestUser = User.GUEST_USER;
        assertTrue(guestUser.isGuestUser());
    }

    @Test
    @DisplayName("이름과 이메일이 같으면 Ture 리턴")
    void name_and_email_equals_return_true() {
        User user1 = new User(new UserAuth("user", "password"), "name", "email@email.com");
        User user2 = new User(new UserAuth("user2", "password2"), "name", "email@email.com");
        assertTrue(user1.equalsNameAndEmail(user2));
    }

    @Test
    @DisplayName("유저 업데이트")
    void user_update() {
        User actual = new User(new UserAuth("user", "password"), "name", "email@email.com");
        User loginUser = new User(new UserAuth("user", "password"), "name", "email@email.com");
        User targetUser = new User(new UserAuth("target", "password"), "target", "target@email.com");
        actual.update(loginUser, targetUser);
        assertTrue(actual.equalsNameAndEmail(targetUser));
    }

    @Test
    @DisplayName("유저가 서로 다르면 True 리턴")
    void not_same_user_return_true() {
        User user1 = new User(new UserAuth("user", "password"), "name", "email@email.com");
        User user2 = new User(new UserAuth("user", "password"), "name2", "email2@email.com");
        assertTrue(user1.isNot(user2));
    }
}
