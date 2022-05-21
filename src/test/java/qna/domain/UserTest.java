package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("name과 email이 같은지 확인한다.")
    @Test
    void equalsNameAndEmail() {
        //given & when
        boolean actual = JAVAJIGI.equalsNameAndEmail(new User("test", "test", "name", "javajigi@slipp.net"));

        //then
        assertThat(actual).isTrue();
    }

}
