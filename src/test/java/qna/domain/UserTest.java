package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("user 저장 테스트")
    void saveUserTest() {
        User savedJavajigi = userRepository.save(JAVAJIGI);
        User savedSanjigi = userRepository.save(SANJIGI);
        assertAll(
                () -> assertThat(savedJavajigi.getId()).isNotNull(),
                () -> assertThat(savedSanjigi.getName()).isEqualTo(SANJIGI.getName())
        );
    }

    @Test
    @DisplayName("user 찾는 테스트")
    void findUserTest() {
        assertAll(
                () -> assertThat(userRepository.findByUserId("1").equals(JAVAJIGI)),
                () -> assertThat(userRepository.findByUserId("2").equals(SANJIGI))
        );
    }
}
