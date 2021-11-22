package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("사용자가 게스트 사용자인지 확인할 수 있다.")
    void guest_게스트_사용자() {
        final User user = User.GUEST_USER;

        assertThat(user.isGuestUser()).isTrue();
    }
}
