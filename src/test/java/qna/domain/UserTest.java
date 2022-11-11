package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("유저")
public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("유저 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
    }
}
