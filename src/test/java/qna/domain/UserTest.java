package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @ParameterizedTest
    @CsvSource(value = {"1:javajigi:password:name:javajigi@slipp.net"}, delimiter = ':')
    void testEquals(Long id, String userId, String password, String name, String email) {
        User user = new User(id, userId, password, name, email);
        assertAll(
            () -> assertThat(user.getId()).isEqualTo(id),
            () -> assertThat(user.getUserId()).isEqualTo(userId),
            () -> assertThat(user.getPassword()).isEqualTo(password),
            () -> assertThat(user.getName()).isEqualTo(name),
            () -> assertThat(user.getEmail()).isEqualTo(email),
            () -> assertThat(user.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
            () -> assertThat(user.getUpdatedAt()).isNull()
        );
    }

    @DisplayName("User 생성시 필수 값을 확인한다")
    @Test
    void testNullArguments() {
        assertThatThrownBy(() ->  new User(1L, null, "password", "name", "javajigi@slipp.net"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->  new User(1L, "javajigi", null, "name", "javajigi@slipp.net"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->  new User(1L, "javajigi", "password", null, "javajigi@slipp.net"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
