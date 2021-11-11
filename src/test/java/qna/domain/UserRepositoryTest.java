package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void testSave() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User savedUser = userRepository.save(user);
        assertAll(
                () -> assertThat(savedUser.getId()).isNotNull(),
                () -> assertThat(savedUser.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(savedUser.getName()).isEqualTo(user.getName()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail())
        );
    }
}
