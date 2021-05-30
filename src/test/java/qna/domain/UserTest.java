package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import qna.TestUtils;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("user_id 가 같은 user 는 같은 user 라 본다.")
    public void equalsTest() {
        User user1 = new User("test", "passsword1", "name1", "email@email.com");
        User user2 = new User("test", "passsword2", "name2", "email@email.com");

        assertThat(user1).isEqualTo(user2);
    }

    @ParameterizedTest
    @DisplayName("유저를 인증할때는 유저 아이디와 비밀번호를 비교한다.")
    @CsvSource(value = {"rightId:rightPassword:true", "wrongId:rightPassword:false",
                        "rightId:wrongPassword:false", "wrongId:wrongPassword:false"}, delimiter = ':')
    void matchLoginUser(String loginUserId, String loginPassword, boolean expected) {
        User user = new User("rightId", "rightPassword", "name", "email@email.com");
        User loginUser = new User(loginUserId, loginPassword, "name", "email@email.com");

        assertThat(user.authenticate(loginUser)).isEqualTo(expected);
    }

    @Test
    @DisplayName("유저의 이름은 20자를 넘을 수 없다.")
    void userNameTest() {
        assertThatThrownBy(() -> new User("userId", "password", TestUtils.createText(21), "email@email.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이름의 최대 길이를 초과했습니다.");
    }

    @Test
    @DisplayName("유저의 이메일은 50자를 넘을 수 없다.")
    void userEmailLengthTest() {
        assertThatThrownBy(() -> new User("userId", "password", "userName", TestUtils.createText(50) + "@email.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이메일의 최대 길이값을 초과했습니다.");
    }

    @Test
    @DisplayName("유저의 이메일은 이메일 형식이어야 한다.")
    void userEmailPatternTest() {
        assertThatThrownBy(() -> new User("userId", "password", "userName", "email"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이메일 형식이 잘못됐습니다.");
    }

    @Test
    @DisplayName("유저 아이디는 20자를 넘을 수 없다.")
    void userIdTest() {
        assertThatThrownBy(() -> new User(TestUtils.createText(21), "password", "userName", "email@email.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("유저 아이디 최대 길이를 초과 했습니다.");
    }

    @Test
    @DisplayName("유저 비밀번호는 20자를 넘을 수 없다.")
    void userPasswordTest() {
        assertThatThrownBy(() -> new User("userId", TestUtils.createText(21), "userName", "email@email.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("유저 비밀번호의 최대 길이를 초과했습니다.");
    }
}
