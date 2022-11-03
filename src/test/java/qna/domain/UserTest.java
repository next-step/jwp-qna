package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
        User savedUser = userRepository.save(JAVAJIGI);

        assertAll(
                () -> assertNotNull(savedUser.getId()),
                () -> assertEquals(savedUser.getUserId(), JAVAJIGI.getUserId()),
                () -> assertEquals(savedUser.getPassword(), JAVAJIGI.getPassword()),
                () -> assertEquals(savedUser.getName(), JAVAJIGI.getName()),
                () -> assertEquals(savedUser.getEmail(), JAVAJIGI.getEmail())
        );
    }

    @DisplayName("findByUserId 검증 성공")
    @Test
    void findByUserId() {
        User expectedUser = userRepository.save(JAVAJIGI);

        User actualUser = userRepository.findByUserId(JAVAJIGI.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        assertAll(
                () -> assertNotNull(actualUser.getId()),
                () -> assertEquals(actualUser.getUserId(), expectedUser.getUserId()),
                () -> assertEquals(actualUser.getPassword(), expectedUser.getPassword()),
                () -> assertEquals(actualUser.getName(), expectedUser.getName()),
                () -> assertEquals(actualUser.getEmail(), expectedUser.getEmail())
        );
    }
}
