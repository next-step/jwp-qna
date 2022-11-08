package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 저장")
    void User_repository_save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("UserRepository Find By UserId Test")
    void User_repository_findByUserId_return() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> result = userRepository.findByUserId(user.getUserId());
        assertThat(result.get()).isEqualTo(user);
    }
}
