package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exceptions.EmptyStringException;
import qna.exceptions.NullStringException;
import qna.exceptions.StringTooLongException;
import qna.exceptions.UnAuthorizedException;

public class UserTest {

    private static final String TEXT_LENGTH_111 = "Length = 111. This value is too long for the column of this entity. Could not execute the insert SQL statement.";

    private static final String USER_ID = "alice";
    private static final String PASSWORD = "password";
    private static final String NAME = "Alice";
    private static final String EMAIL = "alice@mail";

    private User alice = new User(USER_ID, PASSWORD, NAME, EMAIL);

    @DisplayName("사용자 자신의 이름과 이메일을 변경한다.")
    @Test
    void update_AuthorizedUser_Success() {
        User trudy = new User(USER_ID, PASSWORD, "Trudy", "trudy@mail");

        alice.update(alice, trudy);

        assertThat(alice.getName()).isEqualTo(trudy.getName());
        assertThat(alice.getEmail()).isEqualTo(trudy.getEmail());
    }

    @DisplayName("비밀번호가 다르면 사용자 정보를 변경할 수 없다.")
    @Test
    void update_WrongPassword_ExceptionThrown() {
        User trudy = new User(USER_ID, "123456", "Trudy", "trudy@mail");

        assertThatExceptionOfType(UnAuthorizedException.class).isThrownBy(() ->
            alice.update(alice, trudy)
        );
    }

    @DisplayName("다른 사용자의 정보는 변경할 수 없다.")
    @Test
    void update_OtherUser_ExceptionThrown() {
        User trudy = new User("trudy", "123456", "Trudy", "trudy@mail");

        assertThatExceptionOfType(UnAuthorizedException.class).isThrownBy(() ->
            alice.update(alice, trudy)
        );
    }

    @DisplayName("다른 이름이나 이메일은 False 반환")
    @Test
    void equalsNameAndEmail_OtherUser_False() {
        User bob = new User("bob", "123456", "Bob", "bob@mail");
        User trudy = new User("trudy", "123456", "Alice", "trudy@mail");

        assertThat(alice.equalsNameAndEmail(null)).isFalse();
        assertThat(alice.equalsNameAndEmail(bob)).isFalse();
        assertThat(alice.equalsNameAndEmail(trudy)).isFalse();
    }

    @DisplayName("같은 이름, 이메일은 True 반환")
    @Test
    void equalsNameAndEmail_SameNameAndEmail_True() {
        User eve = new User("eve", "123456", "Alice", "alice@mail");

        assertThat(alice.equalsNameAndEmail(eve)).isTrue();
    }

    @DisplayName("Null 문자열 저장 불가")
    @Test
    void create_NullString_ExceptionThrown() {
        assertThatExceptionOfType(NullStringException.class).isThrownBy(() ->
            new User(null, PASSWORD, NAME, EMAIL)
        );
        assertThatExceptionOfType(NullStringException.class).isThrownBy(() ->
            new User(USER_ID, null, NAME, EMAIL)
        );
        assertThatExceptionOfType(NullStringException.class).isThrownBy(() ->
            new User(USER_ID, PASSWORD, null, EMAIL)
        );
    }

    @DisplayName("빈 문자열 저장 불가")
    @Test
    void create_EmptyString_ExceptionThrown() {
        assertThatExceptionOfType(EmptyStringException.class).isThrownBy(() ->
            new User("", PASSWORD, NAME, EMAIL)
        );
        assertThatExceptionOfType(EmptyStringException.class).isThrownBy(() ->
            new User(USER_ID, "", NAME, EMAIL)
        );
        assertThatExceptionOfType(EmptyStringException.class).isThrownBy(() ->
            new User(USER_ID, PASSWORD, "", EMAIL)
        );
    }

    @DisplayName("문자열 길이 제한")
    @Test
    void create_StringTooLong_ExceptionThrown() {
        assertThatExceptionOfType(StringTooLongException.class).isThrownBy(() ->
            new User(TEXT_LENGTH_111, PASSWORD, NAME, EMAIL)
        );
        assertThatExceptionOfType(StringTooLongException.class).isThrownBy(() ->
            new User(USER_ID, TEXT_LENGTH_111, NAME, EMAIL)
        );
        assertThatExceptionOfType(StringTooLongException.class).isThrownBy(() ->
            new User(USER_ID, PASSWORD, TEXT_LENGTH_111, EMAIL)
        );
        assertThatExceptionOfType(StringTooLongException.class).isThrownBy(() ->
            new User(USER_ID, PASSWORD, NAME, TEXT_LENGTH_111)
        );
    }

}
