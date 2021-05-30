package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("User 생성")
    @Test
    public void newUser(){
        assertThat(JAVAJIGI.getId()).isEqualTo(1L);
        assertThat(JAVAJIGI.getUserId()).isEqualTo("javajigi");
        assertThat(JAVAJIGI.getPassword()).isEqualTo("password");
        assertThat(JAVAJIGI.getName()).isEqualTo("name");
        assertThat(JAVAJIGI.getEmail()).isEqualTo("javajigi@slipp.net");
    }

    @DisplayName("수정 시 userId 다를 시 에러 발생")
    @Test
    void updateWithDiffrentUserId(){
        User differentId = new User(1L, "javajigi2", "password", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> JAVAJIGI.update(differentId, SANJIGI))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("수정 시 password 다를 시 에러 발생")
    @Test
    void updateWithDiffrentPassword(){
        User differentPassword = new User(1L, "javajigi", "different", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> JAVAJIGI.update(JAVAJIGI, differentPassword))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
