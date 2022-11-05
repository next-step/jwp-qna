package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name",
        "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name",
        "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        User userSaved = userRepository.save(JAVAJIGI);

        assertAll(
            () -> assertNotNull(userSaved.getId()),
            () -> assertEquals(userSaved.getUserId(), JAVAJIGI.getUserId()),
            () -> assertEquals(userSaved.getPassword(), JAVAJIGI.getPassword()),
            () -> assertEquals(userSaved.getName(), JAVAJIGI.getName()),
            () -> assertEquals(userSaved.getEmail(), JAVAJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("findByUserId 테스트")
   void findByUserIdTest() {
        User userSaved = userRepository.save(UserTest.JAVAJIGI);
        User userFound = userRepository.findByUserId(userSaved.getUserId()).get();

        assertEquals(userSaved, userFound);
    }
}
