package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("저장하면 DB가 생성한 아이디가 있다.")
    @Test
    void save() {
        final User newUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
        final User savedUser = userRepository.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
    }

    @DisplayName("동일한 유저아이디를 두 개 이상 저장할 수 없다. 유저아이디는 유일해야 한다.")
    @Test
    void uniqueUserId() {
        final User newUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
        userRepository.save(newUser);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    final User duplicatedUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
                    userRepository.save(duplicatedUser);
                });
    }

    @DisplayName("유저아이디는 길이 제약을 넘을 수 없다.")
    @Test
    void maxUserId() {
        final String longUserId = "123456789012345678901";
        final User newUser = new User(longUserId, "password", "남동민", "dmut7691@gmail.com");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(newUser));
    }

    @DisplayName("유저비밀번호는 길이 제약을 넘을 수 없다.")
    @Test
    void maxPassword() {
        final String longPassword = "123456789012345678901";
        final User newUser = new User("dominiqn", longPassword, "남동민", "dmut7691@gmail.com");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(newUser));
    }

    @DisplayName("이름은 길이 제약을 넘을 수 없다.")
    @Test
    void maxName() {
        final String longName = "일이삼사오육칠팔구십일이삼사오육칠팔구십일";
        final User newUser = new User("dominiqn", "password", longName, "dmut7691@gmail.com");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(newUser));
    }

    @DisplayName("이메일은 길이 제약을 넘을 수 없다.")
    @Test
    void maxEmail() {
        final String longEmail = "longemail@1234567890123456789012345678901234567890.com";
        final User newUser = new User("dominiqn", "password", "남동민", longEmail);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(newUser));
    }
}
