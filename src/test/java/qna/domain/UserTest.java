package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @DisplayName("user 저장 테스트")
    @Test
    void userSaveTest() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "id", "password", "name", "email");

        User savedUser = userRepository.save(user);

        assertAll(() -> {
            assertThat(savedUser.getId(), is(user.getId()));
            assertThat(savedUser.getUserId(), is(user.getUserId()));
            assertThat(savedUser.getPassword(), is(user.getPassword()));
            assertThat(savedUser.getName(), is(user.getName()));
            assertThat(savedUser.getEmail(), is(user.getEmail()));
            assertTrue(savedUser.getCreatedAt().isEqual(now) || savedUser.getCreatedAt().isAfter(now));
            assertTrue(savedUser.getUpdatedAt().isEqual(now) || savedUser.getUpdatedAt().isAfter(now));
        });
    }
}
