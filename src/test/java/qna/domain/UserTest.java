package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "javajigi", "password1", "name", "javajigi@slipp.net");
        user2 = new User(2L, "sanjigi", "password2", "name", "sanjigi@slipp.net");
    }

    @Test
    @DisplayName("사용자의 패스워드 일치 여부를 리턴한다.")
    void user_패스워드_일치_여부() {
        assertTrue(user1.matchPassword("password1"));
    }

    @Test
    @DisplayName("사용자의 이름과 이메일이 모두 일치하면 true를 리턴한다.")
    void user_이름_이메일_일치_여부() {
        assertTrue(user1.equalsNameAndEmail(new User(2L, "javajigi", "password", "name", "javajigi@slipp.net")));
    }

    @Test
    @DisplayName("사용자 일치 여부를 리턴한다.")
    void user_유저_일치_여부() {
        assertTrue(user1.isNotEquals(new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net")));
    }

    @Test
    @DisplayName("사용자 정보와 로그인 사용자가 일치하지 않으면 예외가 발생한다.")
    void user_수정_시_사용자_확인() {
        assertThrows(UnAuthorizedException.class, () -> user1.update(user2, user1));
    }

    @Test
    @DisplayName("사용자의 패스워드가 일치하지 않으면 예외가 발생한다.")
    void user_수정_시_사용자_패스워드_확인() {
        assertThrows(UnAuthorizedException.class, () -> user1.update(user1, user2));
    }

    @Test
    @DisplayName("사용자의 name, email을 수정한다.")
    void user_수정_성공() {
        User updateUser = new User(3L, "updateId", "password1", "updateUser", "update@slipp.net");
        user1.update(user1, updateUser);

        assertAll(() -> {
            assertThat(user1.getName()).isEqualTo("updateUser");
            assertThat(user1.getEmail()).isEqualTo("update@slipp.net");
        });
    }
}
