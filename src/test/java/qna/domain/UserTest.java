package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        saveAndIdUpdate(JAVAJIGI);
        saveAndIdUpdate(SANJIGI);
    }

    private void saveAndIdUpdate(final User user) {
        final User savedUser = userRepository.save(user);
        user.setId(savedUser.getId());
    }

    @Test
    void User_를_저장_할_경우_저장된_객체와_저장_후_객체가_일치하다() {
        final User user = new User(3L, "doyoung", "password", "name", "doyoung@qna.test");
        final User savedUser = userRepository.save(user);
        assertEquals(savedUser, user);
    }

    @Test
    void 아이디를_통해서_User를_조회할_수_있다() {
        final Optional<User> userOptional = userRepository.findByUserId(JAVAJIGI.getUserId());
        assertAll(() -> {
            assertTrue(userOptional.isPresent());
            assertEquals(userOptional.get(), JAVAJIGI);
        });
    }
}
