package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("두 유저의 id(PK)가 같다면 두 유저는 동일하다")
    @Test
    void identity_test() {
        User user1 = new User(1L, "서정국", "password", "name", "email");
        User user2 = new User(1L, "서정국2", "password", "name", "email");

        assertEquals(user1, user2);
    }
}
