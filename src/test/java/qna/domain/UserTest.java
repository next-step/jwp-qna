package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    void User_를_저장_할_경우_저장된_객체와_저장_후_객체가_일치하다() {
        final User user = create(3L, "doyoung", DUMMY_PASSWORD, DUMMY_NAME, "doyoung@qna.test");
        final User savedUser = userRepository.save(user);
        assertEquals(savedUser, user);
    }

    private User create(long id, String userId, String password, String name, String email) {
        return new User(id, userId, password, name, email);
    }

    @Test
    void 아이디를_통해서_User를_조회할_수_있다() {
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
