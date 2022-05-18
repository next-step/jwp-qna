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
        User beforeUser = new User("userId", "password", "before", "before@test.com");
        User afterUser = new User("userId", "password", "after", "after@test.com");

        beforeUser.update(beforeUser, afterUser);

        assertThat(beforeUser).isEqualTo(afterUser);
    }

    @Test
    void 유저의_패스워드와_유저_아이디가_맞지_않으면_업데이트시_예외가_발생해야_한다() {
        User user = new User("userId", "password", "", "");
        User wrongUserId = new User("wrongUserId", "password", "", "");
        User wrongPasswordUser = new User("userId", "wrongPassword", "", "");
        User targetUser = new User();

        assertThatThrownBy(() -> user.update(wrongUserId, targetUser)).isInstanceOf(UnAuthorizedException.class);
        assertThatThrownBy(() -> user.update(user, wrongPasswordUser)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 유저의_패스워드_일치_여부는_정상적으로_동작해야_한다() {
        String correctPassword = "password";
        String wrongPassword = "wrongPassword";
        User user = new User("", correctPassword, "" ,"");

        assertThat(user.matchPassword(correctPassword)).isTrue();
        assertThat(user.matchPassword(wrongPassword)).isFalse();
    }

    @Test
    void 유저의_이름과_이메일의_일치_여부는_정상적으로_동작해야_한다() {
        String correctName = "correct";
        String correctEmail = "correct@test.com";
        String wrongName = "wrong";
        String wrongEmail = "wrong@test.com";
        User user = new User("", "", correctName, correctEmail);
        User correctUser = new User("", "", correctName, correctEmail);
        User wrong = new User("", "", wrongName, wrongEmail);

        assertThat(user.equalsNameAndEmail(correctUser)).isTrue();
        assertThat(user.equalsNameAndEmail(wrong)).isFalse();
    }

    @Test
    void 게스트_유저인_경우_게스트_여부는_true_이어야_한다() {
        User guest = User.GUEST_USER;

        assertThat(guest.isGuestUser()).isTrue();
    }
}
