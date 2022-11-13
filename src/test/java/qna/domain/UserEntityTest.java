package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 테스트")
public class UserEntityTest {

    @DisplayName("생성 성공")
    @Test
    void create_user_success() {
        //given:
        long id = 1L;
        //when:
        final User user = provideUser(id);
        //then:
        assertThat(user.getId()).isEqualTo(id);
    }

    public static User provideUser() {
        return provideUser(1L);
    }

    public static User provideUser(long id) {
        return new User(id, "mingvel", "password", "name", "dlsqo2005@naver.com");
    }
}
