package qna.domain.wrappers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserEmailTest {

    @Test
    void 이메일_원시값_포장_객체_생성() {
        UserEmail userEmail = new UserEmail("mwkwon@github.com");
        assertThat(userEmail).isEqualTo(new UserEmail("mwkwon@github.com"));
    }

    @Test
    void 이메일_길이가_50_초과_하는_경우_에러_발생() {
        String email ="mwkwon@github.commwkwon@github.commwkwon@github.com";
        assertThatThrownBy(() -> new UserEmail(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력 가능한 길이는 50자 이하만 입력 가능합니다.");
    }
}
