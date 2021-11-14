package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {

    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @Test
    void findById() {
        // when
        User result1 = userRepository.findById(JAVAJIGI.getId()).get();
        User result2 = userRepository.findById(SANJIGI.getId()).get();

        // then
        assertThat(result1).isEqualTo(JAVAJIGI);
        assertThat(result2).isEqualTo(SANJIGI);
    }

    @Test
    void update() {
        // given
        User user = userRepository.findById(JAVAJIGI.getId()).get();
        String newName = "박성민";

        // when
        user.setName(newName);

        // then
        List<User> result = userRepository.findByName(newName);
        assertThat(result).containsExactly(JAVAJIGI);
    }
}
