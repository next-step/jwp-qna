package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserId.MAX_USER_ID_LENGTH;
import static qna.util.TestUtils.createStringLongerThan;

class UserIdTest {

    @Test
    @DisplayName("사용자 ID 정상 생성")
    void user_id_생성() {
        // given
        String userId = "smpark1020";

        // when
        UserId result = new UserId(userId);

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("사용자 ID에 null이 입력될 경우 실패한다.")
    void user_id_null() {
        // when, then
        assertThatThrownBy(() -> new UserId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자 ID는 반드시 입력되어야 합니다. (최대 길이: " + MAX_USER_ID_LENGTH + ")");
    }

    @Test
    @DisplayName("사용자 ID의 길이가 최대 길이보다 긴 경우 생성 실패한다.")
    void user_id_길이_초과() {
        // given
        String userId = createStringLongerThan(MAX_USER_ID_LENGTH);

        // when, then
        assertThatThrownBy(() -> new UserId(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자 ID는 " + MAX_USER_ID_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + MAX_USER_ID_LENGTH + ")");
    }
}