package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("동등성 비교")
    @Test
    void equals() {
        User user1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        User user2 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        Assertions.assertThat(user1.equals(user2)).isTrue();
    }
}
