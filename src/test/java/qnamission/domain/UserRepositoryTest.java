package qnamission.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User expected = new User("name", "password", "userId");
        User actual = userRepository.save(expected);
        assertNotNull(actual.getId());
    }

    @Test
    void saveDuplicatedId() {
        User user1 = new User("name", "password", "userId");
        userRepository.save(user1);

        User user2 = new User("name1", "password1", "userId");
        assertThrows(Exception.class, () -> userRepository.save(user2));
    }
}