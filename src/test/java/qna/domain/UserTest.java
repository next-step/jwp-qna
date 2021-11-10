package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private UserRepository userRepository;

    @Autowired
    public UserTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ParameterizedTest
    @MethodSource("testUserList")
    void testEquals(User user) {
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(savedUser.getUpdatedAt()).isNull();
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

    private static List<User> testUserList() {
        return Arrays.asList(JAVAJIGI, SANJIGI);
    }
}
