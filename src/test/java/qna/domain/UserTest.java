package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.UnAuthorizedException;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "javajigi", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "sanjigi", "sanjigi@slipp.net");

    @Test
    void 기본데이터_Not_Null_검증() {
        // then
        assertAll(
            () -> assertThat(JAVAJIGI.getId()).isNotNull(),
            () -> assertThat(JAVAJIGI.getPassword()).isNotNull(),
            () -> assertThat(JAVAJIGI.getUserId()).isNotNull(),
            () -> assertThat(JAVAJIGI.getName()).isNotNull()
        );
    }

    @Test
    @DisplayName("유저정보 업데이트 후 equalsNameAndEmail 메소드 일치 확인")
    void update_EqualsNameAndEmail_검증() {
        // given
        String changeName = "changeName";
        String changeEmail = "changeEmail@email.co.kr";
        User user = new User("wooobo", "password", "myname", "theHwa@email.com");
        User changeUser = new User("wooobo", "password", changeName, changeEmail);

        // when
        user.setName(changeName);
        user.setEmail(changeEmail);
        user.update(user, changeUser);

        // then
        assertThat(user.equalsNameAndEmail(changeUser)).isTrue();
    }

    @Test
    @DisplayName("User update() 메소드 userId Null 예외 발생 체크")
    void update_matchUserId_userId_NULL_exception() {
        // given
        User loginUserAndUserIdNULL = new User(1L, null, "password", "javajigi", "javajigi@slipp.net");

        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                JAVAJIGI.update(loginUserAndUserIdNULL, JAVAJIGI);
            }).withMessage(UnAuthorizedException.UNAUTHORIZED_EXCEPTION_USER_ID_NULL_MESSAGE);
    }

    @Test
    @DisplayName("User update() 메소드 password 같지 않을 경우 예외 발생 체크")
    void update_matchUserId_password_MISSMATCH_exception() {
        // given
        User targetUserAndMissMatchPW = new User(1L, "javajigi", "change", "javajigi", "javajigi@slipp.net");

        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                JAVAJIGI.update(JAVAJIGI, targetUserAndMissMatchPW);
            }).withMessage(UnAuthorizedException.UNAUTHORIZED_EXCEPTION_MISS_MATCH_PASSWORD_MESSAGE);
    }

}
