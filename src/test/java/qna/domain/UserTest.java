package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User actual = userRepository.save(JAVAJIGI);

        assertThat(actual).isNotNull();
    }

    @Test
    void findById() {
        User expected = userRepository.save(SANJIGI);
        Optional<User> actual = userRepository.findById(expected.getId());

        assertThat(actual).hasValue(expected);
    }
}
