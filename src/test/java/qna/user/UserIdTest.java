package qna.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.TestStringGenerate.generateByLength;

public class UserIdTest {
    @ParameterizedTest
    @ValueSource(ints = {
            1, 20
    })
    @DisplayName("사용자 아이디 생성")
    public void createUserId(int userIdLength) {
        String userId = generateByLength(userIdLength);
        UserId actual = new UserId(userId);
        assertThat(actual).isEqualTo(new UserId(userId));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 21
    })
    @DisplayName("사용자 아이디 생성 - 실패")
    public void createUserId_fail(int userIdLength) {
        String userId = generateByLength(userIdLength);
        assertThatThrownBy(() -> new UserId(userId)).isInstanceOf(IllegalArgumentException.class);
    }
}