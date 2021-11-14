package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User LEWISSEO = new User(3L, "lewisseo", "password", "name", "lewisseo91@random.net");

    @Autowired
    private UserRepository userRepository;
    private User user;
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        now = LocalDateTime.now();
        user = userRepository.save(JAVAJIGI);
    }

    @DisplayName("user 생성")
    @Test
    void saveUserTest() {
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net"),
                () -> assertThat(user.getCreatedAt()).isAfter(now),
                () -> assertThat(user.getUpdatedAt()).isAfter(now)
        );
    }

    @DisplayName("user userId로 찾기")
    @Test
    void findUserByIdTest() {
        assertThat(userRepository.findByUserId("javajigi")).isNotNull();
    }

    @DisplayName("user 수정")
    @Test
    void userUpdateTest() {
        user.setEmail("change_mail@slipp.net");
        User userFromRepository = userRepository.findById(user.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(userFromRepository.getEmail()).isEqualTo("change_mail@slipp.net");
    }

    @DisplayName("user 삭제")
    @Test
    void removeUserTest() {
        assertThat(userRepository.findAll().size()).isEqualTo(1);
        userRepository.delete(user);
        assertThat(userRepository.findAll().size()).isZero();
    }

    @AfterEach
    void beforeFinish() {
        userRepository.flush();
    }

}
