package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = createUser(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = createUser(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("유저아이디 다를 경우 예외 처리 테스트")
    public void updateTest() {
        User user = createUser("DEVELOPYO", "1234", "ljp", "test@test.com");
        User user2 = createUser("TESTID", "1111", "ljp", "test@test.com");
        assertThatThrownBy(() -> user.update(user, user2)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("유저패스워드 다를 경우 예외 처리 테스트")
    public void updateTest2() {
        User user = createUser("DEVELOPYO", "1234", "ljp", "test@test.com");
        User user2 = createUser("DEVELOPYO", "1111", "ljp", "test@test.com");
        assertThatThrownBy(() -> user.update(user, user2)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("유저 이름 혹은 이메일이 다른 경우 테스트")
    public void equalsNameAndEmailTest() {
        User user = createUser("DEVELOPYO", "1234", "ljp", "ljp@test.com");
        User user2 = createUser("DEVELOPYO", "1111", "ljp", "test@test.com");
        User user3 = createUser("DEVELOPYO", "1111", "yhs", "test@test.com");

        assertThat(user.equalsNameAndEmail(user2)).isFalse();
        assertThat(user2.equalsNameAndEmail(user3)).isFalse();
    }

    @Test
    @DisplayName("유저 이름과 이메일이 같은 경우 테스트")
    public void equalsNameAndEmailTest2() {
        User user = createUser("DEVELOPYO", "1234", "ljp", "ljp@test.com");
        User user2 = createUser("DEVELOPYO", "1111", "ljp", "ljp@test.com");

        assertThat(user.equalsNameAndEmail(user2)).isTrue();
    }

    private static User createUser(Long id, String userId, String password, String name, String email) {
        return new User(id, new UserId(userId), new Password(password), new Name(name), new Email(email));
    }

    private static User createUser(String userId, String password, String name, String email) {
        return new User(new UserId(userId), new Password(password), new Name(name), new Email(email));
    }
}
