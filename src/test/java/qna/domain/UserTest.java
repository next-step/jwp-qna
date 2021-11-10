package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public User saveUser(){
        return userRepository.save(JAVAJIGI);
    }

    @AfterEach
    private void afterEach(){
        entityManager
                .createNativeQuery("ALTER TABLE user ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("user 등록")
    public void saveUserTest(){
        User savedUser = saveUser();

        Optional<User> user = userRepository.findById(1L);

        assertEquals(savedUser, user.get());
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
