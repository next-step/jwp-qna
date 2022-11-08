package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.fixture.TestFixture.JAVAJIGI;

@DataJpaTest
public class UserTest {

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
