package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserTest {
    private static final String USER_ID = "user1";
    private static final String PASSWORD = "password2";
    private static final String NAME = "test";
    private static final String EMAIL = "test@test.com";
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    private static final User user1 = new User(USER_ID, PASSWORD, NAME, EMAIL);

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("갱신할 사용자 정보가 주어지면")
        class Context_with_update_user {
            final User updateUser = SANJIGI;

            @Test
            @DisplayName("갱신된 사용자 객체를 리턴한다")
            void it_returns_update_user() {
                User user = JAVAJIGI;
                user.update(JAVAJIGI, updateUser);

                assertThat(user.getName()).isEqualTo(updateUser.getName());
                assertThat(user.getEmail()).isEqualTo(updateUser.getEmail());
            }
        }

        @Nested
        @DisplayName("주어진 사용자와 다른 로그인 사용자 정보가 주어지면")
        class Context_with_different_update_user {
            final User loginUser = user1;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatExceptionOfType(UnAuthorizedException.class)
                        .isThrownBy(() -> loginUser.update(JAVAJIGI, SANJIGI));
            }
        }

        @Nested
        @DisplayName("사용자 비밀번호가 다른 사용자 정보가 주어지면")
        class Context_with_wrong_password {
            final User loginUser = user1;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatExceptionOfType(UnAuthorizedException.class)
                        .isThrownBy(() -> loginUser.update(loginUser, SANJIGI));
            }
        }
    }

    @Nested
    @DisplayName("equalsNameAndEmail 메서드는")
    class Describe_equalsNameAndEmail {
        final User givenUser = JAVAJIGI;

        @Nested
        @DisplayName("유효한 사용자 정보가 주어지면")
        class Context_with_valid_user {
            final User compareUser = JAVAJIGI;

            @Test
            @DisplayName("참을 리턴한다 ")
            void it_returns_true() {
                assertThat(givenUser.equalsNameAndEmail(compareUser)).isTrue();
            }
        }

        @Nested
        @DisplayName("유효하지 않은 사용자 정보가 주어지면")
        class Context_with_invalid_user {
            final User compareUser = null;

            @Test
            @DisplayName("거짓을 리턴한다 ")
            void it_returns_false() {
                assertThat(givenUser.equalsNameAndEmail(compareUser)).isFalse();
            }
        }
    }

    @DisplayName("주어진 비밀번호와 입력받은 비밀번호의 일치여부를 리턴한다.")
    @ParameterizedTest
    @CsvSource({"password, true", "password2, false"})
    void matchPassword(String password, boolean isMatch) {
        assertThat(JAVAJIGI.matchPassword(password)).isEqualTo(isMatch);
    }

    @DisplayName("본인일 경우 게스트 사용자는 거짓을 리턴한다.")
    @Test
    void guestUser() {
        assertThat(JAVAJIGI.isGuestUser()).isFalse();
    }
}
