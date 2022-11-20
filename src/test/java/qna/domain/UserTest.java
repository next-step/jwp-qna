package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("유저 엔티티")
public class UserTest extends JpaSliceTest {
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

    @DisplayName("저장하면 저장한 일시가 생성된다.")
    @Test
    void createdAt() {
        final User newUser = new User("dominiqn", "password", "남동민", "dmut7691@gmail.com");
        final User savedUser = userRepository.save(newUser);

        assertAll(
                () -> assertThat(savedUser.getCreatedAt()).isNotNull(),
                () -> assertThat(savedUser.getUpdatedAt()).isNotNull()
        );
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

    @DisplayName("유저아이디는 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullUserId(final String userId) {
        final User newUser = new User(userId, "password", "남동민", "dmut7691@gmail.com");

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

    @DisplayName("유저비밀번호는 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullPassword(final String password) {
        final User newUser = new User("dominiqn", password, "남동민", "dmut7691@gmail.com");

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

    @DisplayName("이름은 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullName(final String name) {
        final User newUser = new User("dominiqn", "password", name, "dmut7691@gmail.com");

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

    @DisplayName("이메일은 null일 수 있다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullEmail(final String email) {
        final User newUser = new User("dominiqn", "password", "남동민", email);

        assertThatNoException().isThrownBy(() -> userRepository.save(newUser));
    }

    @DisplayName("유저 정보가 수정되면, 수정일시가 변경된다.")
    @Test
    void updatedDateTime() {
        final User user = userRepository.save(new User("dominiqn", "password", "남동민", null));
        final LocalDateTime firstUpdatedAt = user.getUpdatedAt();

        user.setEmail("dmut7691@gmail.com");
        final User updatedUser = userRepository.saveAndFlush(user);

        assertThat(updatedUser.getUpdatedAt()).isNotEqualTo(firstUpdatedAt);
    }

    @DisplayName("유저가 손님인지 아닌지 알 수 있다.")
    @Test
    void isGuest() {
        final User normalUser = new User("dominiqn", "password", "남동민", null);
        final User guest = User.GUEST_USER;

        assertAll(
                () -> assertThat(normalUser.isGuestUser()).isFalse(),
                () -> assertThat(guest.isGuestUser()).isTrue()
        );
    }
}
