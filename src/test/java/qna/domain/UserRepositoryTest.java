package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    private static final User userTest = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.save(userTest);
    }

    @Test
    void findByUserId() {
        User user = userRepository.findByUserId("javajigi").get();
        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getPassword()).isEqualTo("password"),
                () -> assertThat(user.getName()).isEqualTo("name"),
                () -> assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net")
        );
    }
}
