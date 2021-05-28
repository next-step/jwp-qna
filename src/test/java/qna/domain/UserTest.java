package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        User user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        // when & then
        assertAll(
            () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
            () -> assertThat(user.getName()).isEqualTo(JAVAJIGI.getName()),
            () -> assertThat(user.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
            () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("비밀번호 비교 테스트")
    void matchPassword() {
        // given & when & then
        assertThat(JAVAJIGI.matchPassword("test")).isFalse();
        assertThat(JAVAJIGI.matchPassword(JAVAJIGI.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이름과 이메일 비교 테스트")
    void equalsNameAndEmail() {
        // given
        User test = new User(2L, "test", "1234", "테스트", "test@test.com");
        // when & then
        assertThat(JAVAJIGI.equalsNameAndEmail(null)).isFalse();
        assertThat(JAVAJIGI.equalsNameAndEmail(test)).isFalse();
        assertThat(JAVAJIGI.equalsNameAndEmail(JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given & when
        String name = "test";
        String email = "test@test.com";
        User targetUser = new User(2L, JAVAJIGI.getUserId(), JAVAJIGI.getPassword(), name, email);
        JAVAJIGI.update(JAVAJIGI, targetUser);
        // then
        assertThat(JAVAJIGI.getName()).isEqualTo(name);
        assertThat(JAVAJIGI.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("수정(아이디가 다른 경우) 예외 테스트")
    void update_not_equals_user_id_exception() {
        // given
        User targetUser = new User(2L, "test2", "password2", "name", "test2@test.com");
        // when & then
        Assertions.assertThrows(UnAuthorizedException.class, () -> JAVAJIGI.update(targetUser, targetUser));
    }

    @Test
    @DisplayName("수정(패스워드가 다른 경우) 예외 테스트")
    void update_not_equals_password_exception() {
        // given
        User targetUser = new User(2L, "test2", "password2", "name", "test2@test.com");
        // when & then
        Assertions.assertThrows(UnAuthorizedException.class, () -> JAVAJIGI.update(JAVAJIGI, targetUser));
    }
}
