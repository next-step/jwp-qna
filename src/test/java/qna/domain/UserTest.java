package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class UserTest {
    public static final User JAVAJIGI = new User( "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("userId와 password가 동일하면 name과 email 변경이 가능하다.")
    @Test
    void update() {
        User user = new User("kim", "password", "kim", "kim@korea.com");
        User target = new User("kim", "password", "lee", "lee@korea.com");

        user.update(user, target);

        assertAll(
                () -> assertThat(user.getName()).isEqualTo(target.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(target.getEmail())
        );
    }

    @DisplayName("update시 userId가 틀리면 UnAuthorizedExceptiondl 발생한다.")
    @Test
    void update_user_id_fail() {
        User user = new User("kim", "password", "kim", "kim@korea.com");
        User target = new User("lee", "password", "lee", "lee@korea.com");

        assertThatThrownBy(() -> user.update(target, target)).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("update시 password가 틀리면 UnAuthorizedException이 발생한다.")
    @Test
    void update_password_fail() {
        User user = new User("kim", "password", "kim", "kim@korea.com");
        User target = new User("kim", "passwordFail", "lee", "lee@korea.com");

        assertThatThrownBy(() -> user.update(target, target)).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("user간 name과 email이 모두 같으면 참이다.")
    @Test
    void equals_name_and_email() {
        User user = new User("kim", "password", "kim", "kim@korea.com");
        User target = new User("lee", "password", "kim", "kim@korea.com");

        assertThat(user.equalsNameAndEmail(target)).isTrue();
    }
}
