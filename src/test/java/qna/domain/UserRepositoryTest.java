package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    private void beforeEach() {
        savedUser = userRepository.save(JAVAJIGI);
    }

    @Test
    @DisplayName("user 등록")
    public void saveUserTest() {
        User user = userRepository.findById(savedUser.getId()).get();
        assertAll(
                () -> assertNotNull(savedUser.getId()),
                () -> assertEquals(user, savedUser)
        );
    }

    @Test
    @DisplayName("user id로 검색")
    public void findByUserIdTest() {
        Optional<User> oUser = userRepository.findByUserId(JAVAJIGI.getUserId());
        assertAll(
                () -> assertTrue(oUser.isPresent()),
                () -> assertEquals(oUser.get(), savedUser)
        );
    }

    @Test
    @DisplayName("찾는 user id가 없을 경우")
    public void findByUserIdNotFoundTest() {
        Optional<User> oUser = userRepository.findByUserId(SANJIGI.getUserId());

        assertFalse(oUser.isPresent());
    }
}