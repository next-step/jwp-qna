package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 도메인 생성 테스트")
    void generate01(){
        // given & when
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        em.flush();
        em.clear();

        // then
        Optional<User> findJavajigi = userRepository.findByUserId(javajigi.getUserId());
        Optional<User> findSanjigi = userRepository.findByUserId(sanjigi.getUserId());

        assertAll(
            () -> assertTrue(findJavajigi.isPresent()),
            () -> assertEquals(javajigi, findJavajigi.get()),
            () -> assertTrue(findSanjigi.isPresent()),
            () -> assertEquals(sanjigi, findSanjigi.get())
        );
    }
}
