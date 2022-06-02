package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("name, email변경 시 로그인 정보와 userId가 일치하지 않으면 오류를 발생시킨다.")
    void checkExceptionWithInvalidUserId() {
        User loginUser = new User(JAVAJIGI.getId(), "eunji", JAVAJIGI.getPassword(), JAVAJIGI.getName(), JAVAJIGI.getEmail());
        assertThatThrownBy(() -> JAVAJIGI.update(loginUser, SANJIGI)).isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("name, email변경 시 로그인 정보와 password가 일치하지 않으면 오류를 발생시킨다.")
    void checkExceptionWithInvalidPassword() {
        User targetUser = new User(JAVAJIGI.getId(), JAVAJIGI.getUserId(), "eunji-abc", "eunji", "eunji@abc.com");
        assertThatThrownBy(() -> JAVAJIGI.update(JAVAJIGI, targetUser)).isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("로그인 한 JAVAJIGI는 SANJIGI의 email로 정보를 변경한다.")
    void checkUpdate() {
        JAVAJIGI.update(JAVAJIGI, SANJIGI);
        assertThat(JAVAJIGI.getEmail()).isEqualTo(SANJIGI.getEmail());
    }
}
