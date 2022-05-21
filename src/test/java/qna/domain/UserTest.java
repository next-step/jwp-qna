package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("정보 변경 - 로그인 사용자 불일치")
    void update_invalidLoginUser() {
        User targetUser = new User(3L, "jigijigi", "password", "name", "jigijigi@slipp.net");
        assertThatThrownBy(() -> JAVAJIGI.update(SANJIGI, targetUser))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("정보 변경 - 비밀번호 불일치")
    void update_invalidPassword() {
        User targetUser = new User(3L, "jigijigi", "invalidpassword", "name", "jigijigi@slipp.net");
        assertThatThrownBy(() -> JAVAJIGI.update(JAVAJIGI, targetUser))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
