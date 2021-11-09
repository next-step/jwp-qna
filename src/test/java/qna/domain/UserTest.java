package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    public User saveUser(){
        return userRepository.save(JAVAJIGI);
    }

    @Test
    @DisplayName("user 등록")
    public void saveUserTest(){
        User savedUser = saveUser();

        assertEquals(savedUser, JAVAJIGI);
    }

    @Test
    @DisplayName("user id로 검색")
    public void findByUserIdTest(){
        User savedUser = saveUser();

        Optional<User> oUser = userRepository.findByUserId("javajigi");

        assertTrue(oUser.isPresent());
        assertEquals(oUser.get(), savedUser);
    }

    @Test
    @DisplayName("찾는 user id가 없을 경우")
    public void findByUserIdNotFoundTest(){
        saveUser();

        Optional<User> oUser = userRepository.findByUserId("sanjigi");

        assertFalse(oUser.isPresent());
    }
}
