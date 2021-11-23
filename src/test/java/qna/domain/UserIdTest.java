package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserIdTest {
    private static final String LENGTH_20_TEXT = "12345678901234567890";

    @DisplayName("유저ID 생성")
    @Test
    void constructUserId_success() {
        UserId userId = new UserId(LENGTH_20_TEXT);
        assertThat(userId).isEqualTo(new UserId(LENGTH_20_TEXT));
    }

    @DisplayName("잘못된 형식으로 유저ID 생성 시 에러")
    @Test
    void constructNullUserId_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new UserId(null))
            .withMessage("길이 20이하의 비어있지 않은 유저ID를 입력해주세요.");
    }

    @DisplayName("잘못된 형식으로 유저ID 생성 시 에러")
    @Test
    void constructUserIdWithLength101_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new UserId(LENGTH_20_TEXT + 1))
            .withMessage("길이 20이하의 비어있지 않은 유저ID를 입력해주세요.");
    }
}