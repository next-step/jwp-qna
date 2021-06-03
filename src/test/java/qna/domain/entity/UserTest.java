package qna.domain.entity;

import org.junit.jupiter.api.*;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
    public static final User USER_JAVAJIGI = new User(1L, "javajigi", "password", "javajigi", "javajigi@slipp.net");
    public static final User USER_SANJIGI = new User(2L, "sanjigi", "password", "sanjigi", "sanjigi@slipp.net");

    @Test
    @Order(1)
    @DisplayName("사용자 정보 확인")
    void check() {
        assertAll(
            () -> assertThat(USER_JAVAJIGI.getId()).isEqualTo(1L),
            () -> assertThat(USER_JAVAJIGI.getUserId()).isEqualTo("javajigi"),
            () -> assertThat(USER_JAVAJIGI.getPassword()).isEqualTo("password"),
            () -> assertThat(USER_JAVAJIGI.getName()).isEqualTo("javajigi"),
            () -> assertThat(USER_JAVAJIGI.getEmail()).isEqualTo("javajigi@slipp.net")
        );
    }

    @Test
    @DisplayName("사용자 동등성")
    void isEqualTo() {
        assertThat(USER_JAVAJIGI).isEqualTo(User.builder().id(1L).build());
    }

    @Test
    @DisplayName("사용자 정보 업데이트 성공")
    void updateSucess() {
        //When
        User login = User.builder()
                .userId("javajigi")
                .build();

        User update = User.builder()
                .name("포비").email("javajigi@naver.com")
                .password("password")
                .build();


        USER_JAVAJIGI.update(login, update);

        assertAll(
            () -> assertThat(USER_JAVAJIGI.getName()).isEqualTo("포비"),
            () -> assertThat(USER_JAVAJIGI.getEmail()).isEqualTo("javajigi@naver.com")
        );
    }

    @Test
    @DisplayName("사용자 정보 업데이트 실패")
    void updateFail() {
        assertAll(
            () -> assertThrows(UnAuthorizedException.class, () -> {
                User login = User.builder().userId("hun").build();
                User update = User.builder().password("password").build();

                USER_JAVAJIGI.update(login, update);
            }),
            () -> assertThrows(UnAuthorizedException.class, () -> {
                User login = User.builder().userId("javajigi").build();
                User update = User.builder().password("1").build();

                USER_JAVAJIGI.update(login, update);
            })
        );
    }
}