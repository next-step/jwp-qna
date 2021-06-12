package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    void create() {
        userRepository.save(JAVAJIGI);
        assertThat(JAVAJIGI.getId()).isEqualTo(1L);
    }

    @Test
    void findByUserId() {
        userRepository.save(JAVAJIGI);
        Optional<User> user = userRepository.findByUserId("javajigi");

        assertThat(user.isPresent()).isTrue();
    }
}
