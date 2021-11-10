package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import qna.common.CommonRepositoryTest;

class UserRepositoryTest extends CommonRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("save 메서드로 생성하면 같은 객체를 반환하는지 확인한다")
    @Test
    void save() {
        // given
        User user = createUser("user1", "123", "user1", "user@email.com");

        // when
        User savedUser = userRepository.save(user);

        // then
        assertEquals(user, savedUser);
    }

    @DisplayName("같은 userId 로 여러번 생성할 경우 exception 이 발생한다")
    @Test
    void saveUserIdUniqueException() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            String userId = "user1";
            userRepository.save(createUser(userId));
            userRepository.save(createUser(userId));
        });
    }

    @DisplayName("User 를 아이디로 찾는다")
    @Test
    void findById() {
        // given
        User savedUser = userRepository.save(createUser("user1"));

        // when
        User findUser = userRepository.findById(savedUser.getId()).orElse(null);

        // then
        assertNotNull(findUser);
        assertEquals(savedUser, findUser);
    }

    @DisplayName("User 를 userId 로 찾는다")
    @Test
    void findByUserId() {
        // given
        User savedUser = userRepository.save(createUser("user1"));

        // when
        User findUser = userRepository.findByUserId(savedUser.getUserId()).orElse(null);

        // then
        assertNotNull(findUser);
        assertEquals(savedUser, findUser);
    }

    private User createUser(String userId) {
        return createUser(userId, "123", "user1", "user@email.com");
    }

    private User createUser(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }
}
