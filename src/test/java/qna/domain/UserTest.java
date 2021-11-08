package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("User 를 생성하여 저장한다.")
    @Test
    void save() {
        // when
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        em.flush();
        em.clear();

        // then
        Optional<User> findJavajigi = userRepository.findByUserId(javajigi.getUserId());
        Optional<User> findSanjigi = userRepository.findByUserId(sanjigi.getUserId());

        assertTrue(findJavajigi.isPresent());
        assertEquals(javajigi, findJavajigi.get());

        assertTrue(findSanjigi.isPresent());
        assertEquals(sanjigi, findSanjigi.get());
    }
}
