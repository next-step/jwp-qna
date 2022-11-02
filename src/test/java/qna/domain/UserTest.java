package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("사용자 클래스 테스트")
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void update_로그인_유저가_현재_userId와_다르면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            JAVAJIGI.update(SANJIGI, new User(1L, "javajigi", "password", "name2", "name2@test.com"));
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void update_업데이트하려는_정보가_현재_password와_다르면_UnAuthorizedException_발생() {
        assertThatThrownBy(() -> {
            JAVAJIGI.update(JAVAJIGI, new User(1L, "javajigi", "password_change", "name", "javajigi@slipp.net"));
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
