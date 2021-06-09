package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    public void setUp() {
        javajigi = userRepository.save(JAVAJIGI);
        sanjigi = userRepository.save(SANJIGI);
    }

    @DisplayName("save 확인")
    @Test
    public void save() {
        assertAll(
                () -> assertThat(javajigi.getId()).isNotNull(),
                () -> assertThat(javajigi.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(sanjigi.getId()).isNotNull(),
                () -> assertThat(sanjigi.getUserId()).isEqualTo(sanjigi.getUserId())
        );
    }

    @DisplayName("findByUserId 확인")
    @Test
    public void findByUserId() {
        Optional<User> findJavajigi = userRepository.findByUserId("javajigi");
        Optional<User> findSanjigi = userRepository.findByUserId("sanjigi");
        assertThat(findJavajigi.get()).isSameAs(javajigi);
        assertThat(findSanjigi.get()).isSameAs(sanjigi);
    }
}
