package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void save() {
        assertAll(
                () -> assertDoesNotThrow(() -> userRepository.save(JAVAJIGI)),
                () -> assertDoesNotThrow(() -> userRepository.save(SANJIGI))
        );
    }
}
