package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private User user = UserRepositoryTest.JAVAJIGI;

    @Test
    @DisplayName("사용자 ID가 같은 경우 true를 리턴한다.")
    void matchId_true() {
        // when
        boolean result = user.matchId(user.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자 ID가 다른 경우 false를 리턴한다.")
    void matchId_false() {
        // when
        boolean result = user.matchId(UserRepositoryTest.SANJIGI.getId());

        // then
        assertThat(result).isFalse();
    }
}
