package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotUpdateException;
import qna.exception.UnAuthorizedException;

public class UserTest {

    private User JAVAJIGI;
    private User JAVAJIGI_NEW_ID;
    private User JAVAJIGI_NEW_DATA;
    private User SANJIGI;
    private User SANJIGI_NEW_PASSWORD;

    private Question q1;

    @BeforeEach
    void setup() {
        JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        JAVAJIGI_NEW_ID = new User(1L, "javajigi_new", "password", "name", "javajigi@slipp.net");
        JAVAJIGI_NEW_DATA = new User(1L, "javajigi", "password", "new", "new@slipp.net");
        SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        SANJIGI_NEW_PASSWORD = new User(2L, "sanjigi", "password_new", "name", "sanjigi@slipp.net");
        q1 = new Question(1L, "title", "content").writeBy(JAVAJIGI);
    }


    @DisplayName("유저 정보 변경시 유저의 아이디와 로그인 유저의 아이디와 같지 않으면 UnAuthorizedException 발생")
    @Test
    void update_wrongUserId_exception() {

        assertThatThrownBy(() -> JAVAJIGI.update(JAVAJIGI_NEW_ID, JAVAJIGI))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("유저 정보 변경시 유저의 비밀번호와 타겟 유저의 비밀번호와 같지 않으면 UnAuthorizedException 발생")
    @Test
    void update_wrongPassword_exception() {
        assertThatThrownBy(() -> SANJIGI.update(SANJIGI, SANJIGI_NEW_PASSWORD))
                .isInstanceOf(UnAuthorizedException.class);

    }

    @DisplayName("로그인 유저의 아이디와 타겟 유저의 비밀번호가 유저 아이디 비밀번호와 동일하면 이름과 이메일 변경이 가능하다")
    @Test
    void update_test() {

        JAVAJIGI.update(JAVAJIGI, JAVAJIGI_NEW_DATA);
        assertAll(
                () -> assertEquals(JAVAJIGI.getName(), JAVAJIGI_NEW_DATA.getName()),
                () -> assertEquals(JAVAJIGI.getEmail(), JAVAJIGI_NEW_DATA.getEmail())
        );
    }

    @DisplayName("유저의 이름과 이메일이 같은지 확인한다")
    @Test
    void equalsNameAndEmail_test() {
        assertTrue(SANJIGI.equalsNameAndEmail(SANJIGI_NEW_PASSWORD));
    }

    @DisplayName("게스트 유저를 확인할 수 있다")
    @Test
    void isGuestUser_test() {
        User guest = User.GUEST_USER;
        assertAll(
                () -> assertFalse(JAVAJIGI.isGuestUser()),
                () -> assertTrue(guest.isGuestUser())
        );
    }

    @DisplayName("유저의 삭제이력을 추가할 수 있다.")
    @Test
    void addDeleteHistory_test() {
        DeleteHistory d1 = DeleteHistory.ofQuestion(q1);
        assertFalse(JAVAJIGI.getDeleteHistories().contains(d1));

        assertEquals(d1.getDeletedBy(), JAVAJIGI);
    }

    @DisplayName("유저의 삭제이력을 추가할때 작성자가 다르면 CannotUpdateException에러를 발생시킨다.")
    @Test
    void addDeleteHistory_exception() {
        DeleteHistory d1 = DeleteHistory.ofQuestion(q1);
        assertEquals(d1.getDeletedBy(), JAVAJIGI);
        assertThatThrownBy(() -> SANJIGI.addDeleteHistory(d1)).isInstanceOf(CannotUpdateException.class);
    }
}
