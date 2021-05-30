package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


    @DisplayName("id/name/email을 점검하는지 확인한다")
    @Test
    void check_equals_method() {

        User onlyEmailDifferentUser = new User(1L,"javajigi", "password", "name", "fakeJavaJigi@slipp.net");
        User onlyNameDifferentUser = new User(2L, "javajigi", "password", "fakeName", "javajigi@slipp.net");
        assertThat(JAVAJIGI.equals(onlyEmailDifferentUser)).isFalse();
        assertThat(JAVAJIGI.equals(onlyNameDifferentUser)).isFalse();
    }
}
