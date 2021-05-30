package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


    @DisplayName("id 값만으로 equals() 여부를 점검하는지 확인한다")
    @Test
    void check_equals_method(){
        User onlyIdDifferentUser = new User(99L, "javajigi", "password", "name", "javajigi@slipp.net");
        User onlyIdSameUser = new User(1L, "pythonjigi", "pythonPW","pythonName","python@slipp.net");
        assertThat(JAVAJIGI.equals(onlyIdDifferentUser)).isFalse();
        assertThat(JAVAJIGI.equals(onlyIdSameUser)).isTrue();
    }
}
