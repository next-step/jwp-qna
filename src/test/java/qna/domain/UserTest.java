package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
