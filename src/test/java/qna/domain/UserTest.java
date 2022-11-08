package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("User Update loginUser UserId error 테스트")
    void User_Update_Login_User_Id_Error() {
        assertThatThrownBy(() -> {
            User targetUser = new User("sanjigi", "password", "name", "javajigi@slipp.net");
            SANJIGI.update(JAVAJIGI, targetUser);
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("User Update loginUser Password error 테스트")
    void User_Update_Login_User_Password_Error() {
        assertThatThrownBy(() -> {
            User targetUser = new User("javajigi", "wrongpassword", "name", "javajigi@slipp.net");
            JAVAJIGI.update(JAVAJIGI, targetUser);
        }).isInstanceOf(UnAuthorizedException.class);
    }
}
