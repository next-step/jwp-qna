package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthenticationException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User GUEST = User.GUEST_USER;

    private static User user;
    private static User target;

    @BeforeEach
    void beforeEach() {
        user = new User(3L, "user", "password", "user", "user@qna.co.kr");
        target = new User("target", "password", "target", "target@qna.co.kr");
    }

    @DisplayName("일반 회원, 게스트 회원을 구분할 수 있다.")
    @Test
    void classify() {
        assertThat(JAVAJIGI.isGuestUser()).isFalse();
        assertThat(GUEST.isGuestUser()).isTrue();
    }

    @DisplayName("게스트 회원은 이름과 이메일을 변경할 수 없다.")
    @Test
    void fail() {
        assertThatThrownBy(() -> GUEST.update(GUEST, SANJIGI)).isInstanceOf(UnAuthenticationException.class);
    }

    @DisplayName("일반 회원은 이름과 이메일을 변경할 수 있다.")
    @Test
    void change() {
        user.update(user, target);
        assertThat(user.equalsNameAndEmail(target)).isTrue();
    }

    @DisplayName("아이디가 일치하지 않는 경우 이름과 이메일을 변경할 수 없다.")
    @Test
    void userId() {
        assertThatThrownBy(() -> user.update(GUEST, target)).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("패스워드가 일치하지 않는 경우 이름과 이메일을 변경할 수 없다.")
    @Test
    void password() {
        user.changePassword("guest");
        assertThatThrownBy(() -> user.update(user, target)).isInstanceOf(UnAuthorizedException.class);
    }
}
