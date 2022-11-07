package qna.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        User user = UserTest.JAVAJIGI;
        User userSaved = userRepository.save(user);

        assertAll(
            () -> assertNotNull(userSaved.getId()),
            () -> assertEquals(userSaved.getUserId(), user.getUserId()),
            () -> assertEquals(userSaved.getPassword(), user.getPassword()),
            () -> assertEquals(userSaved.getName(), user.getName()),
            () -> assertEquals(userSaved.getEmail(), user.getEmail())
        );
    }

    @Test
    @DisplayName("findByUserId 테스트")
    void findByUserIdTest() {
        User userSaved = userRepository.save(UserTest.JAVAJIGI);
        User userFound = userRepository.findByUserId(userSaved.getUserId()).get();

        assertEquals(userSaved, userFound);
    }
}
