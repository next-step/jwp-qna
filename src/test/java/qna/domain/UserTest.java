package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


    @DisplayName("equals() 수행 시, user_id를 점검하는지 확인한다")
    @Test
    void check_equals_method() {

        User onlyEmailDifferentUser = new User("javajigi", "password", "name", "pythonjigi@slipp.net");
        User onlyUserIdDifferentUser = new User("pythonjigi", "password", "name", "javajigi@slipp.net");
        assertThat(JAVAJIGI.equals(onlyEmailDifferentUser)).isTrue();
        assertThat(JAVAJIGI.equals(onlyUserIdDifferentUser)).isFalse();
    }
}
