package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.Password.MAX_PASSWORD_LENGTH;
import static qna.util.TestUtils.createStringLongerThan;

class PasswordTest {

    @Test
    @DisplayName("비밀번호 정상 생성")
    void password_생성() {
        // given
        String password = "1234";

        // when
        Password result = new Password(password);

        // then
        assertThat(result.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("비밀번호에 null이 입력될 경우 실패한다.")
    void password_null() {
        // when, then
        assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 반드시 입력되어야 합니다. (최대 길이: " + MAX_PASSWORD_LENGTH + ")");
    }

    @Test
    @DisplayName("비밀번호의 길이가 최대 길이보다 긴 경우 생성 실패한다.")
    void password_길이_초과() {
        // given
        String password = createStringLongerThan(MAX_PASSWORD_LENGTH);

        // when, then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 " + MAX_PASSWORD_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + MAX_PASSWORD_LENGTH + ")");
    }
}