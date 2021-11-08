package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import qna.UnAuthorizedException;

@DisplayName("사용자")
public class UserTest {

    public static final User JAVAJIGI =
        User.of(1L, "javajigi", "password", "name", "javajigi@slipp.net");

    public static final User SANJIGI =
        User.of(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> User.of("id", "password", "name", "email"));
    }

    @ParameterizedTest(name = "{displayName}[{index}] if user id is {0}, can not be instanced")
    @DisplayName("id가 비어있는 상태로 객체화하면 IllegalArgumentException")
    @NullAndEmptySource
    void instance_nullUserId_thrownIllegalArgumentException(String emptyUserId) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> User.of(emptyUserId, "password", "name", "email"))
            .withMessage("'userId' must not be empty");

    }

    @ParameterizedTest(name = "{displayName}[{index}] if password is {0}, can not be instanced")
    @DisplayName("패스워드가 비어있는 상태로 객체화하면 IllegalArgumentException")
    @NullAndEmptySource
    void instance_nullPassword_thrownIllegalArgumentException(String emptyPassword) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> User.of("user id", emptyPassword, "name", "email"))
            .withMessage("'password' must not be empty");
    }

    @ParameterizedTest(name = "{displayName}[{index}] if name is {0}, can not be instanced")
    @DisplayName("이름이 비어있는 상태로 객체화하면 IllegalArgumentException")
    @NullAndEmptySource
    void instance_nullName_thrownIllegalArgumentException(String emptyName) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> User.of("user id", "password", emptyName, "email"))
            .withMessage("'name' must not be empty");
    }

    @ParameterizedTest(name = "{displayName}[{index}] it is {1} that 'pass' password match '{0}'")
    @DisplayName("패스워드 동일치 여부")
    @CsvSource({"pass,true", "notMatch,false"})
    void matchPassword(String password, boolean expected) {
        //given
        User user = User.of("id", "pass", "name", "email");

        //when
        boolean matchPassword = user.matchPassword(password);

        //then
        assertThat(matchPassword)
            .isEqualTo(expected);
    }

    @ParameterizedTest(name = "{displayName}[{index}] it is {2} that user('name', 'email) equals user('{0}', '{1}')")
    @DisplayName("유저의 이메일과 이름 동일 여부")
    @CsvSource({"name,email,true", "notEqualsName,email,false", "name,notEqualsEmail,false",
        "notEqualsName,notEqualsEmail,false"})
    void equalsNameAndEmail(String name, String email, boolean expected) {
        //given
        User user = User.of("id", "pass", "name", "email");

        //when
        boolean equalsNameAndEmail = user.equalsNameAndEmail(User.of("id2", "pass", name, email));

        //then
        assertThat(equalsNameAndEmail)
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("유저 이메일과 이름 변경")
    void update() {
        //given
        User user = User.of("id", "password", "name", "email");
        User target = User.of("id", "password", "updatedName", "updatedEmail");

        //when
        user.update(user, target);

        //then
        assertThat(user.equalsNameAndEmail(target))
            .isTrue();
    }

    @ParameterizedTest(name = "{displayName}[{index}] user('id', 'password') can not updated by login user id({0}) and target password({1})")
    @CsvSource({"notMatchId,password", "id,notMatchPassword", "notMatchId,notMatchPassword"})
    @DisplayName("id, password 가 다른 경우 이메일과 이름 변경하면 UnAuthorizedException")
    void update_notMathId_thrownUnAuthorizedException(String userId, String password) {
        //given
        User user = User.of("id", "password", "name", "email");
        User loginUser = User.of(userId, "password", "name", "email");
        User target = User.of("id", password, "updatedName", "updatedEmail");

        //when
        ThrowingCallable updateCall = () -> user.update(loginUser, target);

        //then
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(updateCall);
    }
}
