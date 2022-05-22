package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


    @Test
    @DisplayName("생성자 테스트")
    void User_create() {
        assertThat(JAVAJIGI)
            .isEqualTo(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @Test
    @DisplayName("이름과 이메일이 같은지 테스트")
    void equalsNameAndEmail() {
        User loginUser = new User("javajigi", "password", "name", "javajigi@slipp.net");
        assertThat(JAVAJIGI.equalsNameAndEmail(null)).isFalse();
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isFalse();
        assertThat(JAVAJIGI.equalsNameAndEmail(loginUser)).isTrue();
    }

    @Test
    @DisplayName("User update 메소드 유효성 검사")
    void update_실패() {
        User loginUser = new User("test", "password", "name", "email");
        User target = new User("javajigi", "test", "name", "email");
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> JAVAJIGI.update(loginUser, target));
    }

}
