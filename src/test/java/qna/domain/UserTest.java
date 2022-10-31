package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    
    @Test
    void User_field_test() {
        assertAll(
                () -> assertThat(JAVAJIGI.getId()).isEqualTo(1L),
                () -> assertThat(JAVAJIGI.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(JAVAJIGI.getName()).isEqualTo(SANJIGI.getName()),
                () -> assertThat(JAVAJIGI.getPassword()).isEqualTo(SANJIGI.getPassword()),
                () -> assertThat(JAVAJIGI.getEmail()).isEqualTo("javajigi@slipp.net")
        );
    }
}
