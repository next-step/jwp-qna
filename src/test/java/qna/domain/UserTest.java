package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("User Update 테스트")
    void User_update(){
        User updatedUser = new User("javajigi", "password", "newname", "newemail@slipp.net");
        JAVAJIGI.update(JAVAJIGI, updatedUser);
        assertAll(
                () -> assertThat(JAVAJIGI.getName()).isEqualTo("newname"),
                () -> assertThat(JAVAJIGI.getEmail()).isEqualTo("newemail@slipp.net")
        );
    }

    @Test
    @DisplayName("User Update 실패 테스트: userId 불일치")
    void User_update_fail_userId_불일치(){
        assertThatThrownBy(() -> {
            User updatedUser = new User("javajigi", "password", "newname", "newemail@slipp.net");
            SANJIGI.update(JAVAJIGI, updatedUser);
        });
    }

    @Test
    @DisplayName("User Update 실패 테스트: password 불일치")
    void User_update_fail_password_불일치(){
        assertThatThrownBy(() -> {
            User updatedUser = new User("javajigi", "wrongpassword", "newname", "newemail@slipp.net");
            JAVAJIGI.update(JAVAJIGI, updatedUser);
        });
    }
}
