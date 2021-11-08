package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private static User saved;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        saved = userRepository.save(new User("minseoklim", "1q2w3e4r!", "임민석", "mslim@slipp.net"));
    }

    @Test
    void save() {
        User actual = userRepository.save(SANJIGI);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.equalsNameAndEmail(SANJIGI)).isTrue();
    }

    @Test
    void identity() {
        User actual = userRepository.findById(saved.getId()).get();
        assertThat(actual == saved).isTrue();
    }

    @Test
    void update() {
        User expected = userRepository.findById(saved.getId()).get();
        expected.setName("박재성");
        expected.setEmail("minseoklim@slipp.net");

        User actual = userRepository.findById(saved.getId()).get();
        assertThat(actual.equalsNameAndEmail(expected)).isTrue();
    }

    @Test
    void delete() {
        userRepository.deleteById(saved.getId());
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> userRepository.findById(saved.getId()).get()
        );
    }

    @Test
    void findByUserId() {
        String expected = saved.getUserId();
        String actual = userRepository.findByUserId(expected).get().getUserId();
        assertThat(actual).isEqualTo(expected);
    }
}
