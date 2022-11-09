package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void 값_검증() {
        assertAll(
                () -> assertThat(JAVAJIGI.getId()).isEqualTo(1L),
                () -> assertThat(SANJIGI.getId()).isEqualTo(2L),
                () -> assertThat(JAVAJIGI.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(SANJIGI.getUserId()).isEqualTo("sanjigi"),
                () -> assertThat(JAVAJIGI.getPassword()).isEqualTo("password"),
                () -> assertThat(SANJIGI.getPassword()).isEqualTo("password"),
                () -> assertThat(JAVAJIGI.getName()).isEqualTo("name"),
                () -> assertThat(SANJIGI.getName()).isEqualTo("name"),
                () -> assertThat(JAVAJIGI.getEmail()).isEqualTo("javajigi@slipp.net"),
                () -> assertThat(SANJIGI.getEmail()).isEqualTo("sanjigi@slipp.net")
        );
    }
}
