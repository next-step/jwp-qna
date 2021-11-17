package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.common.exception.UnAuthorizedException;

public class UserTest {

    public static final User JAVAJIGI = createUser("javajigi", "password", "javajigi",
        new Email("javajigi@slipp.net"));
    public static final User SANJIGI = createUser("sanjigi", "password", "sanjigi",
        new Email("sanjigi@slipp.net"));

    @Test
    void 기본데이터_Not_Null_검증() {
        // then
        assertAll(
            () -> assertThat(JAVAJIGI.getPassword()).isNotNull(),
            () -> assertThat(JAVAJIGI.getUserId()).isNotNull(),
            () -> assertThat(JAVAJIGI.getName()).isNotNull()
        );
    }

    @Test
    @DisplayName("name 과 email 업데이트 후 equalsNameAndEmail 메소드로 변경 확인")
    void update() {
        // given
        User actual = createUser("wooobo", "password", "myname",
            new Email("taeHwa@email.com"));
        User expect = createUser("wooobo", "password", "changeName",
            new Email("changeEmail@email.co.kr"));

        // when
        actual.update(actual, expect);

        // then
        assertThat(actual.equalsNameAndEmail(expect)).isTrue();
    }

    @Test
    @DisplayName("User update() 메소드 userId Null 예외 발생 체크")
    void update_matchUserId_userId_NULL_exception() {
        // given

        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                JAVAJIGI.update(SANJIGI, JAVAJIGI);
            }).withMessage(UnAuthorizedException.UNAUTHORIZED_EXCEPTION_USER_ID_NOT_SAME_MESSAGE);
    }

    @Test
    @DisplayName("User update() 메소드 password 같지 않을 경우 예외 발생 체크")
    void update_matchUserId_password_MISSMATCH_exception() {
        // given
        User targetUserAndMissMatchPW = UserTest.createUser("javajigi", "change",
            "javajigi",
            new Email("javajigi@slipp.net"));

        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                JAVAJIGI.update(JAVAJIGI, targetUserAndMissMatchPW);
            })
            .withMessage(UnAuthorizedException.UNAUTHORIZED_EXCEPTION_MISS_MATCH_PASSWORD_MESSAGE);
    }


    @Test
    @DisplayName("GUEST_USER 는 isGuestUser 가 true 입니다")
    void isGuestUser() {
        // given
        User guestUser = User.GUEST_USER;

        // when
        boolean actual = guestUser.isGuestUser();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("User - Email type 아닐 경우 예외 발생")
    void not_email_save_fail() {
        assertThatExceptionOfType(RuntimeException.class) // then
            .isThrownBy(() -> {
                // when
                UserTest.createUser("javajigi", "change", "javajigi",
                    new Email("javajigi"));
            });
    }


    public static User createUser(String userId, String password, String name, Email email) {
        return new User(new UserAuth(userId, password), new UserData(name, email));
    }
}
