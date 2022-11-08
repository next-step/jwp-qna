package qna.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals() {
        UserId actual = UserId.of("1");
        UserId expected = UserId.of("1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void notEquals() {
        UserId actual = UserId.of("1");
        UserId expected = UserId.of("2");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

}
