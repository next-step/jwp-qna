package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("findByUserId 테스트")
    @Test
    void findByUserId() {
        User savedUser = userRepository.save(UserTest.SANJIGI);

        Optional<User> userOptional = userRepository.findByUserId("sanjigi");
        User user = userOptional.get();

        assertAll(
                () -> assertThat(user.getId()).isEqualTo(savedUser.getId()),
                () -> assertThat(user.getUserId()).isEqualTo(savedUser.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(savedUser.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(savedUser.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(savedUser.getEmail())
        );
    }

}
