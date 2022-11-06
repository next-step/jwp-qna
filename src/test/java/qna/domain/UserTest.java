package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("사용자 클래스 테스트")
public class UserTest {
    public static final User JAVAJIGI =
            new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI =
            new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User user;
    private User loginUser;
    private  User target;

    @BeforeEach
    void setUp() {
        user = new User("user", "password", "user", "user@test.com");
        loginUser = new User("user1", "password", "user1", "user1@test.com");
        target = new User("user1", "password", "user2", "user2@test.com");
    }

    @Test
    void user_객체_생성시_userId가_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
           new User(null, "password", "name", "email");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void user_객체_생성시_password가_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new User("user", null, "name", "email");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void user_객체_생성시_name이_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new User("user", "password", null, "email");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_로그인_유저의_userId가_현재_userId와_다르면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            user.update(loginUser, target);
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void update_로그인_유저의_password가_현재_password와_다르면_UnAuthorizedException_발생() {
        loginUser = new User("user", "password_change", "user", "user@test.com");

        assertThatThrownBy(() -> {
            user.update(loginUser, target);
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void equalsNameAndEmail_현재_유저_객체의_이름과_이메일이_일치() {
        assertTrue(JAVAJIGI.equalsNameAndEmail(new User("testUser", "password", "name", "javajigi@slipp.net")));
    }

    @ParameterizedTest
    @CsvSource(value = { "test:javajigi@slipp.net", "name:test@test.com" }, delimiter = ':')
    void equalsNameAndEmail_현재_유저_객체의_이름과_이메일이_일치하지_않음(String name, String email) {
        assertFalse(JAVAJIGI.equalsNameAndEmail(new User("testUser", "password", name, email)));
    }

    @Test
    void user_동등성_테스트() {
        assertAll(
                () -> assertEquals(
                        new User(1L, "user1", "password", "user1", "user1@test.com"),
                        new User(1L, "user1", "password", "testUser", "testUser@test.com")
                ),
                () -> assertNotEquals(
                        new User(1L, "user1", "password", "user1", "user1@test.com"),
                        new User(1L, "user2", "password", "testUser", "testUser@test.com")
                )
        );
    }
}
