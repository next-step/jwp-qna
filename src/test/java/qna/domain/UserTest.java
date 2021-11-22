package qna.domain;

import static org.assertj.core.api.Assertions.*;

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
        User user = new User(JAVAJIGI.getUserId(), JAVAJIGI.getPassword(), JAVAJIGI.getName(), JAVAJIGI.getEmail());
        User actual = userRepository.save(user);

        assertThat(actual).isNotNull();
    }

    @Test
    void findByUserId() {
        User user = new User(JAVAJIGI.getUserId(), JAVAJIGI.getPassword(), JAVAJIGI.getName(), JAVAJIGI.getEmail());
        User expected = userRepository.save(user);
        User actual = userRepository.findByUserId(JAVAJIGI.getUserId()).get();

        assertThat(expected).isEqualTo(actual);
        assertThat(expected).isSameAs(actual);
    }
}
