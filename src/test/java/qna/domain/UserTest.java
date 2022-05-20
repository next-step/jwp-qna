package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User MOND = new User("mond", "password", "gun", "mond@gmail.com");
    public static final User SRUGI = new User("srugi", "password", "bbu", "srugi@gmail.com");

    @Test
    @DisplayName("객체 생성 확인")
    void verifyUser() {
        User idNullUser = new User("javajigi", "password", "name", "javajigi@slipp.net");

        assertAll(
                () -> assertThat(JAVAJIGI).isEqualTo(JAVAJIGI),
                () -> assertThat(JAVAJIGI).isNotEqualTo(idNullUser)
        );
    }
}
