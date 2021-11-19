package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.Email.MAX_EMAIL_LENGTH;
import static qna.util.TestUtils.createStringLongerThan;

class EmailTest {

    @Test
    @DisplayName("이메일 정상 생성")
    void email_생성() {
        // given
        String email = "smpark911020@gmail.com";

        // when
        Email result = new Email(email);

        // then
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일의 길이가 최대 길이보다 긴 경우 생성 실패한다.")
    void email_생성실패() {
        // given
        String email = createStringLongerThan(MAX_EMAIL_LENGTH);

        // when, then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일은 " + MAX_EMAIL_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + email + ")");
    }
}