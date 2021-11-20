package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("User 값 확인")
    @Test
    void init() {
        User javaJigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");

        assertAll(
            () -> assertThat(javaJigi.getId()).isEqualTo(1L),
            () -> assertThat(javaJigi.getUserId()).isEqualTo("javajigi"),
            () -> assertThat(javaJigi.getPassword()).isEqualTo("password"),
            () -> assertThat(javaJigi.getName()).isEqualTo("name"),
            () -> assertThat(javaJigi.getEmail()).isEqualTo("javajigi@slipp.net")
        );
    }
}
