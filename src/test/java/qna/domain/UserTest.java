package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("유저 저장 성공")
    @Test
    void 유저저장_성공() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        flushAndClear();

        User javajigi = userRepository.findById(1L).get();
        User sanjigi = userRepository.findById(2L).get();
        assertAll(
                () -> assertThat(javajigi.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(sanjigi.getUserId()).isEqualTo("sanjigi")
        );
    }

    @DisplayName("유저 갱신 성공")
    @Test
    void 유저_갱신_성공() {
        userRepository.save(SANJIGI);
        userRepository.save(JAVAJIGI);
        flushAndClear();

        SANJIGI.update(SANJIGI, JAVAJIGI);
        userRepository.save(SANJIGI);
        userRepository.save(JAVAJIGI);
        flushAndClear();

        User sanjigi = userRepository.findByUserId(SANJIGI.getUserId()).get();
        assertThat(sanjigi.getEmail()).isEqualTo(JAVAJIGI.getEmail());
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
