package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User LEWISSEO = new User("lewisseo", "password", "name", "lewisseo91@random.net");

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(JAVAJIGI);
    }

    @DisplayName("user 생성")
    @Test
    void saveUserTest() {
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net")
        );
    }

    @DisplayName("user 수정")
    @Test
    void userUpdateTest() {
        user.changeEmail("change_mail@slipp.net");
        User userFromRepository = userRepository.findById(user.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(userFromRepository.getEmail()).isEqualTo("change_mail@slipp.net");
    }

    @AfterEach
    void beforeFinish() {
        userRepository.flush();
    }

}
