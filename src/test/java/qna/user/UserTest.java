package qna.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("사용자 생성")
    public void createUserTest() {
        assertThat(JAVAJIGI).isEqualTo(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @Test
    @DisplayName("사용자 생성 - null data")
    public void createUserTest_fail() {
        assertAll(
                () -> assertThatThrownBy(() -> new User(1L, null, "password", "name", "javajigi@slipp.net"))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new User(1L, "userId", null, "name", "javajigi@slipp.net"))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new User(1L, "userId", "password", null, "javajigi@slipp.net"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

}
