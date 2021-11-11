package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {


    private static final String DUMMY_PASSWORD = "password";
    private static final String DUMMY_NAME = "name";
    public static final User JAVAJIGI = new User(1L, "javajigi", DUMMY_PASSWORD, DUMMY_NAME, "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", DUMMY_PASSWORD, DUMMY_NAME, "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() {
        userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
    }

    @Test
    @DisplayName("User 를 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        final User user = create(3L, "doyoung", DUMMY_PASSWORD, DUMMY_NAME, "doyoung@qna.test");
        final User savedUser = userRepository.save(user);
        assertEquals(savedUser, user);
    }

    private User create(long id, String userId, String password, String name, String email) {
        return new User(id, userId, password, name, email);
    }

    @Test
    @DisplayName("아이디를 통해서 User를 조회할 수 있다")
    void findByUserId() {
        final Optional<User> userOptional = userRepository.findByUserId(JAVAJIGI.getUserId());
        assertAll(() -> {
            assertTrue(userOptional.isPresent());
            assertEquals(userOptional.get(), JAVAJIGI);
        });
    }

    @AfterAll
    void clear() {
        userRepository.deleteAllInBatch();
    }
}
