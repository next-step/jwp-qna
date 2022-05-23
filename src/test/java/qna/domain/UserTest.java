package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

@DisplayName("유저에 대한 단위 테스트")
class UserTest {

    private User user;
    private User other;

    @BeforeEach
    void setUp() {
        user = new User(1L, "woobeen", "password", "name", "drogba02@naver.com");
        other = new User(2L, "other", "other-password", "name", "other@naver.com");
    }

    @DisplayName("유저의 id 가 다른 user 로 update 를 시도하면 예외가 발생해야 한다")
    @Test
    void user_update_test() {
        User owner = user;

        assertThatThrownBy(() -> {
            owner.update(other, other);
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("패스워드가 다른 user 가 update 를 시도하면 예외가 발생해야 한다")
    @Test
    void user_update_test2() {
        User owner = user;

        assertThatThrownBy(() -> {
            owner.update(owner, other);
        }).isInstanceOf(UnAuthorizedException.class);
    }
}
