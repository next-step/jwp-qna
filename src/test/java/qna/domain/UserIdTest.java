package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    @DisplayName("userId가 null 이면 예외를 던진다.")
    void fromException1() {
        Assertions.assertThatThrownBy(() -> UserId.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("userId는 20자 초과이면 예외를 던진다.")
    void fromException2() {
        Assertions.assertThatThrownBy(() -> UserId.from("123456789012345678901"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals 테스트 (동일한 경우)")
    void equals1() {
        UserId user1 = UserId.from("user1");
        UserId user2 = UserId.from("user1");
        Assertions.assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("equals 테스트 (동일하지 않은 경우)")
    void equals2() {
        UserId user1 = UserId.from("user1");
        UserId user2 = UserId.from("user2");
        Assertions.assertThat(user1).isNotEqualTo(user2);
    }
}