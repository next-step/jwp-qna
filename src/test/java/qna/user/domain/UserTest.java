package qna.user.domain;

import org.junit.jupiter.api.Test;
import qna.user.exception.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void 유저를_업데이트하면_적용되어야_한다() {
        User beforeUser = User.builder()
                .name("before")
                .email("before@test.com")
                .password("password")
                .userId("userId")
                .build();
        User afterUser = User.builder()
                .name("after")
                .email("after@test.com")
                .password("password")
                .userId("userId")
                .build();

        beforeUser.update(beforeUser, afterUser);

        assertThat(beforeUser).isEqualTo(afterUser);
    }

    @Test
    void 유저의_패스워드와_유저_아이디가_맞지_않으면_업데이트시_예외가_발생해야_한다() {
        User user = User.builder()
                .password("password")
                .userId("userId")
                .build();
        User wrongUserId = User.builder()
                .password("password")
                .userId("wrongUserId")
                .build();
        User wrongPasswordUser = User.builder()
                .password("wrongPassword")
                .userId("userId")
                .build();
        User targetUser = User.builder().build();

        assertThatThrownBy(() -> user.update(wrongUserId, targetUser)).isInstanceOf(UnAuthorizedException.class);
        assertThatThrownBy(() -> user.update(user, wrongPasswordUser)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 유저의_패스워드_일치_여부는_정상적으로_동작해야_한다() {
        String correctPassword = "password";
        String wrongPassword = "wrongPassword";
        User user1 = User.builder()
                .password(correctPassword)
                .build();

        assertThat(user1.matchPassword(correctPassword)).isTrue();
        assertThat(user1.matchPassword(wrongPassword)).isFalse();
    }

    @Test
    void 유저의_이름과_이메일의_일치_여부는_정상적으로_동작해야_한다() {
        String correctName = "correct";
        String correctEmail = "correct@test.com";
        String wrongName = "wrong";
        String wrongEmail = "wrong@test.com";
        User user1 = User.builder()
                .name(correctName)
                .email(correctEmail)
                .build();

        User correctUser = User.builder()
                .name(correctName)
                .email(correctEmail)
                .build();
        User wrong = User.builder()
                .name(wrongName)
                .email(wrongEmail)
                .build();

        assertThat(user1.equalsNameAndEmail(correctUser)).isTrue();
        assertThat(user1.equalsNameAndEmail(wrong)).isFalse();
    }

    @Test
    void 게스트_유저인_경우_게스트_여부는_true_이어야_한다() {
        User guest = User.GUEST_USER;

        assertThat(guest.isGuestUser()).isTrue();
    }
}
