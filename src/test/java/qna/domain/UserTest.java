package qna.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User HAGI = new User(3L, "jbh1230", "Password", "byunghak jang", "byunghakjang1230@gmail.com");

    private User loginUser;

    @BeforeEach
    public void beforeEach() {
        this.loginUser = new User(1L, "jbh1230", "Password", "byunghak jang", "byunghakjang1230@gmail.com");
    }

    @Test
    @DisplayName("User 생성")
    void create() {
        // given
        User user = new User(1L, "jbh1230", "Password", "byunghak jang", "byunghakjang1230@gmail.com");

        // then
        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("사용자 정보 복사")
    void new_create() {
        User user = User.copy(this.loginUser);
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(this.loginUser.getId()),
                () -> assertThat(user.getEmail()).isEqualTo(this.loginUser.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(this.loginUser.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(this.loginUser.getPassword()),
                () -> assertThat(user.getUserId()).isEqualTo(this.loginUser.getUserId())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"Password,true", "password,false"})
    @DisplayName("사용자 ID 일치여부 확인")
    void matchedUserId(String targetPassword, boolean result) {
        assertThat(this.loginUser.matchPassword(targetPassword)).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource(value = {"byunghak jang:byunghakjang1230@gmail.com:true", "jang:byunghakjang1230@gmail.com:false",
            "byunghak jang:jang@gmail.com:false", "jang:jang@gmail.com:false"}, delimiter = ':')
    @DisplayName("사용자 이름, 이메일 일치여부")
    void match_name_and_email(String name, String email, boolean result) {
        // given
        User targetUser = new User(0L, "jbh1234", "pw", name, email);

        // then
        assertThat(this.loginUser.equalsNameAndEmail(targetUser)).isEqualTo(result);
    }

    @Test
    @DisplayName("게스트 사용자 여부")
    void guest_user() {
        User guestUser = User.GUEST_USER;
        assertAll(
                () -> assertThat(guestUser.isGuestUser()).isTrue(),
                () -> assertThat(this.loginUser.isGuestUser()).isFalse()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"java:java@gmail.com", "code:code@gmail.com"}, delimiter = ':')
    @DisplayName("이름, 이메일 수정")
    void user_name_and_email_update(String name, String email) {
        // given
        User targetUser = new User(this.loginUser.getUserId(), this.loginUser.getPassword(), name, email);

        // when
        this.loginUser.update(this.loginUser, targetUser);

        // then
        assertAll(
                () -> assertThat(this.loginUser.getName()).isEqualTo(name),
                () -> assertThat(this.loginUser.getEmail()).isEqualTo(email)
        );
    }
}
