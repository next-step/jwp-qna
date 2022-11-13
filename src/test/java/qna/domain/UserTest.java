package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.message.UserMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    public static User userSample(Long id) {
        return new User(id, "shshon", "password", "손상훈", "shshon@naver.com");
    }

    @Test
    @DisplayName("유저를 생성한다")
    void create_user_test() {
        User user = userSample(1L);
        assertThat(user).isEqualTo(new User(1L, "shshon", "password", "손상훈", "shshon@naver.com"));
    }

    @Test
    @DisplayName("유저 생성시 Id가 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_user_without_id_test() {
        assertThatThrownBy(() -> new User(null, "password", "손상훈", "shshon@naver.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(UserMessage.ERROR_USER_ID_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("유저 생성시 패스워드가 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_user_without_password_test() {
        assertThatThrownBy(() -> new User("shshon", null, "손상훈", "shshon@naver.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(UserMessage.ERROR_PASSWORD_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("유저 생성시 이름이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_user_without_name_test() {
        assertThatThrownBy(() -> new User("shshon", "password", null, "shshon@naver.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(UserMessage.ERROR_NAME_SHOULD_BE_NOT_NULL.message());
    }
}
