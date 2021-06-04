package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void 사용자가_같은지_여부_확인() {
        User user = UserTest.JAVAJIGI;
        assertThat(user.isSameUser(UserTest.JAVAJIGI)).isEqualTo(true);
        assertThat(user.isSameUser(UserTest.SANJIGI)).isEqualTo(false);
    }
}
