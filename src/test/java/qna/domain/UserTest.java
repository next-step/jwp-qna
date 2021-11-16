package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net", LocalDateTime.now());
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net", LocalDateTime.now());

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User actual = userRepository.save(JAVAJIGI);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(JAVAJIGI.getId());
    }
}
