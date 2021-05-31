package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {
    private static final String USER_ID = "user1";

    @Test
    void createNameWithValidInput() {
        UserId userId = new UserId(USER_ID);

        assertThat(userId.getUserId()).isEqualTo(USER_ID);
    }

    @ParameterizedTest
    @ValueSource(strings = "abcdefghijklmnopqrstuvabcdefghijklmnopqrstuv")
    @NullAndEmptySource
    void createNameWithInValidInput(String input) {
        assertThatThrownBy(() -> new UserId(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주어진 userId와 다른 UserId 입력시 예외를 던진다")
    @Test
    void matchUserId() {
        // given
        UserId userId = new UserId(USER_ID);
        User user2 = new User("user2", "password2", "name", "test@test.com");

        // then
        assertThatThrownBy(() -> userId.matchUserIdWith(user2))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
