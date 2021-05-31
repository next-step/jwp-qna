package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {
    private static final String PASSWORD = "password";

    @DisplayName("유효한 비밀번호로 비밀번호 객체 생성")
    @Test
    void createWithValidPassword() {
        Password password = new Password(PASSWORD);

        assertThat(password.getPassword()).isEqualTo(PASSWORD);
    }

    @DisplayName("유효하지 않은 비밀번호로 비밀번호 객체 생성")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "abcdefghijklmnopqrjsabcdefghijklmnopqrjs")
    void createWithInValidEmail(String input) {
        assertThatThrownBy(() -> new Password(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주어진 비밀번호와 다른 비밀번호 입력시 예외를 던진다")
    @Test
    void matchPassword() {
        // given
        Password password = new Password(PASSWORD);
        User user1 = new User("user1", "password2", "name", "test@test.com");

        // then
        assertThatThrownBy(() -> password.matchPasswordWith(user1))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
