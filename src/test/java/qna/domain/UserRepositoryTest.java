package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User expected = new User("userId", "password", "name", "email");
        User actual = userRepository.save(expected);
        assertNotNull(actual.getId());
    }

    @Test
    void saveDuplicatedId() {
        User user1 = new User("userId", "password1", "name1", "email1");
        userRepository.save(user1);
        userRepository.flush();

        User user2 = new User("userId", "password2", "name2", "email2");
        assertThrows(Exception.class, () -> userRepository.save(user2));
    }

}