package qna.repository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(new User("iamsojung", "password", "sojung", "email@gmail.com"));
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).get().isEqualTo(user);
    }

    @Test
    @DisplayName("findByUserId 테스트")
    void findByUserIdTest() {
        User userFound = userRepository.findByUserId(user.getUserId()).get();
        assertAll(
            () -> assertThat(userFound.getId()).isNotNull(),
            () -> assertThat(user.equalsNameAndEmail(userFound)).isTrue()
        );
    }
}
