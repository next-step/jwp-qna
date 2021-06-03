package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User loginUser;
    private User target;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "kim", "password", "name", "kim@slipp.net");
        target = new User(1L, "kim", "password", "change", "change@slipp.net");
    }

    @Test
    @DisplayName("같은 사용자인 경우 이메일과 이름을 변경할 수 있다.")
    void update_test() {
        //when
        loginUser.update(loginUser, target);

        //then
        assertAll(
                () -> assertThat(loginUser.getName()).isEqualTo("change"),
                () -> assertThat(loginUser.getEmail()).isEqualTo("change@slipp.net")
        );
    }

    @Test
    @DisplayName("같은 사용자가 아닌 경우 수정 요청 시 예외처리 한다.")
    void update_fail_test() {
        //when
        assertThrows(UnAuthorizedException.class,
                () -> JAVAJIGI.update(loginUser, target)
        );
    }

    @Test
    @DisplayName("이름과 이메일이 같은지 검증한다.")
    void equalsNameAndEmail_test() {
        //given
        User another = new User("another", "123456", "name", "kim@slipp.net");

        //when
        boolean isEqualsNameAndEmail = loginUser.equalsNameAndEmail(another);

        //then
        assertThat(isEqualsNameAndEmail).isTrue();
    }
}
