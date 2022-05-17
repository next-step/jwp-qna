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
    void findByUserId() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);

        User actual = userRepository.findByUserId(JAVAJIGI.getUserId()).get();

        assertAll(
                () -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId())
        );
    }
}
