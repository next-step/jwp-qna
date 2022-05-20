package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    void generate01() {
        // given & when
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        em.flush();
        em.clear();

        // then
        Optional<User> findJavajigi = userRepository.findByUserId(javajigi.userId());
        Optional<User> findSanjigi = userRepository.findByUserId(sanjigi.userId());

        assertAll(
            () -> assertTrue(findJavajigi.isPresent()),
            () -> assertEquals(javajigi, findJavajigi.get()),
            () -> assertTrue(findSanjigi.isPresent()),
            () -> assertEquals(sanjigi, findSanjigi.get())
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("id() 메소드를 통해 User 도메인의 id를 조회할 수 있다.")
    void public_method_01(long id) {
        // given & when
        User user = new User(id, "javajigi", "password", "name", "javajigi@slipp.net");

        // then
        assertThat(user.id()).isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    @DisplayName("userId() 메소드를 통해 User 도메인의 userId를 조회할 수 있다.")
    void public_method_02(String userid) {
        // given & when
        User user = new User(1L, userid, "password", "name", "javajigi@slipp.net");

        // then
        assertThat(user.userId()).isEqualTo(userid);
    }
}
