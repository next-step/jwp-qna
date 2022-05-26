package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "javajigi", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "sanjigi", "sanjigi@slipp.net");

    @AfterEach
    void resetUser() {
        JAVAJIGI.setName("javajigi");
        JAVAJIGI.setEmail("javajigi@slipp.net");

        SANJIGI.setName("sanjigi");
        SANJIGI.setEmail("sanjigi@slipp.net");
    }

    @Test
    @DisplayName("유저 정보를 수정한다.")
    void update() {
        JAVAJIGI.update(JAVAJIGI, SANJIGI);

        assertThat(JAVAJIGI.getName())
                .isEqualTo(SANJIGI.getName());
        assertThat(JAVAJIGI.getEmail())
                .isEqualTo(SANJIGI.getEmail());
    }

    @Test
    @DisplayName("유저 정보 수정 시 로그인 유저의 유저ID와 불일치하면 예외를 발생시킨다.")
    void UnAuthorizedException_matchUserId() {
        User wrongLoginUser = new User(1L,  "javajigi#", "password", "javajigi", "javajigi@slipp.net");

        assertThatThrownBy(() -> JAVAJIGI.update(wrongLoginUser, SANJIGI))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("유저 정보 수정 시 패스워드가 불일치하면 예외를 발생시킨다.")
    void UnAuthorizedException_matchPassword() {
        User targetUser = new User(2L, "target", "password1", "target name", "target@slipp.net");

        assertThatThrownBy(() -> SANJIGI.update(SANJIGI, targetUser))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("패스워드가 일치하는지 확인한다.")
    void matchUserId() {
        assertThat(JAVAJIGI.matchPassword("password"))
                .isTrue();
        assertThat(JAVAJIGI.matchPassword("pasword"))
                .isFalse();
    }

    @Test
    @DisplayName("이름과 이메일로 유저의 일치여부를 판단한다.")
    void equalsNameAndEmail() {
        User equalUser = new User("javajigi", "password", "javajigi", "javajigi@slipp.net");
        User wrongUser = new User("javajigi", "password", "javajava", "javajigi@slipp.net");

        assertThat(JAVAJIGI.equalsNameAndEmail(equalUser))
                .isTrue();
        assertThat(JAVAJIGI.equalsNameAndEmail(wrongUser))
                .isFalse();
    }

}
