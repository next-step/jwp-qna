package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void save() {
        User expected = new User("userId", "password", "name", "email");
        User actual = userRepository.save(expected);
        assertNotNull(actual.getId());
    }

    @Test
    @Transactional
    void saveDuplicatedId() {
        User user1 = new User("userId", "password1", "name1", "email1");
        userRepository.save(user1);

        User user2 = new User("userId", "password2", "name2", "email2");
        assertThrows(Exception.class, () -> userRepository.save(user2));
    }

}