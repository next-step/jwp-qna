package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("update의 결과 첫번째 user 객체의 아이디가 같고 두번째 user 객체의 암호가 같으면 두번째 user 객체의 이름과 이메일로 정보가 변경된다")
    void update() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final User user2 = new User(4L, "mins99", "5678", "test", "mins99@slipp.net");
        final User user3 = new User(5L, "mins999", "1234", "test2", "mins999@slipp.net");
        final String name = user.getName();
        final String email = user.getEmail();

        // when
        user.update(user2, user3);
        final String name2 = user.getName();
        final String email2 = user.getEmail();

        // then
        assertAll(
                () -> assertThat(name).isNotEqualTo(name2),
                () -> assertThat(email).isNotEqualTo(email2)
        );
    }

    @Test
    @DisplayName("유저아이디가 다른 경우 update를 진행하면 오류를 반환한다")
    void invalidUserId() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final User user2 = new User(4L, "mins999", "5678", "test", "mins99@slipp.net");
        final User user3 = new User(5L, "mins999", "1234", "test2", "mins999@slipp.net");

        // when & then
        assertThatThrownBy(() -> user.update(user2, user3))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("유저암호가 다른 경우 update를 진행하면 오류를 반환한다")
    void invalidUserPassword() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final User user2 = new User(4L, "mins99", "5678", "test", "mins99@slipp.net");
        final User user3 = new User(5L, "mins99", "5678", "test2", "mins999@slipp.net");

        // when & then
        assertThatThrownBy(() -> user.update(user2, user3))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("matchPassword의 결과 user의 암호와 입력받은 값의 일치 여부를 반환한다")
    void matchPassword() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");

        // when
        final boolean actual = user.matchPassword("1234");

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("equalsNameAndEmail의 결과 user와 입력받은 유저의 name과 email의 일치 여부를 반환한다")
    void equalsNameAndEmail() {
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final User user2 = new User(4L, "bambi", "0506", "ms", "mins99@slipp.net");

        boolean actual = user.equalsNameAndEmail(user2);

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("equalsNameAndEmail의 결과 null 입력의 경우 거짓을 반환한다")
    void equalsNameAndEmailInputNull() {
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");

        final boolean actual = user.equalsNameAndEmail(null);

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("isGuestUser의 결과 거짓을 반환한다")
    void isGuestUser() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");

        // when
        final boolean actual = user.isGuestUser();

        // then
        assertThat(actual).isFalse();
    }
}
