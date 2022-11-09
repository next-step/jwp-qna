package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("유저 동등성 비교 확인")
    @Test
    void equals() {
        User user1 = JAVAJIGI;
        User user2 = JAVAJIGI;

        assertThat(user1).isEqualTo(user2);
    }
}
