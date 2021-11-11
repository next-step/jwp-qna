package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.UnAuthorizedException;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "javajigi", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "sanjigi", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;

    @Test
    void save() {
        // when
        User actual = users.save(JAVAJIGI);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName())
        );
    }

    @Test
    void identity() {
        // given
        User actual = users.save(JAVAJIGI);

        // when
        User DB조회 = users.findById(actual.getId()).get();

        assertThat(actual).isEqualTo(DB조회);
    }

    @Test
    @DisplayName("유저정보 업데이트 후 equalsNameAndEmail 일치 확인")
    void update_EqualsNameAndEmail_검증() {
        // given
        String changeName = "changeName";
        String changeEmail = "changeEmail@email.co.kr";
        User user = new User("wooobo", "password", "myname", "theHwa@email.com");
        User changeUser = new User("wooobo", "password", changeName, changeEmail);

        // when
        users.save(user);
        User loginUser = users.findByUserId(user.getUserId()).get();

        loginUser.setName(changeName);
        loginUser.setEmail(changeEmail);
        loginUser.update(loginUser, changeUser);

        // then
        User DB조회 = users.findByUserId(user.getUserId()).get();
        assertThat(DB조회.equalsNameAndEmail(changeUser)).isTrue();
    }

    @Test
    void update_matchUserId_userId_NULL_exception() {
        // given
        User loginUserAndUserIdNULL = new User(1L, null, "password", "javajigi", "javajigi@slipp.net");

        //
        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                JAVAJIGI.update(loginUserAndUserIdNULL, JAVAJIGI);
            }).withMessage(UnAuthorizedException.UNAUTHORIZED_EXCEPTION_USER_ID_NULL_MESSAGE);
    }

    @Test
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
