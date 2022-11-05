package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        User actual = new User(1L, "user1", "password", "name", "test@email.com");
        User expected = new User(1L, "user1", "password", "name", "test@email.com");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        User actual = new User(1L, "user1", "password", "name", "test@email.com");
        User expected = new User(2L, "user1", "password", "name", "test@email.com");

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }
}
