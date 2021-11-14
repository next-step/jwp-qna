package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    @Test
    void save() {
        final User actual = userRepository.save(user);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isNotNull(),
            () -> assertThat(actual.getPassword()).isNotNull(),
            () -> assertThat(actual.getName()).isNotNull(),
            () -> assertThat(actual.getEmail()).isNotNull()
        );
    }

    @Test
    void findByUserId() {
        userRepository.save(user);
        final User actual = userRepository.findByUserId(user.getUserId())
            .orElseThrow(NoSuchElementException::new);
        assertThat(actual.getUserId()).isEqualTo(user.getUserId());
    }
}