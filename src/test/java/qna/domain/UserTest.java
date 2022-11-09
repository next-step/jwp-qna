package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User user = userRepository.save(JAVAJIGI);

        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getId()).isNotNull()
        );
    }

    @Test
    void saveAndFind() {
        String expectValue = "javajigi";
        userRepository.save(JAVAJIGI);

        User user = userRepository.findById(1L).get();

        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(expectValue)
        );
    }
}
