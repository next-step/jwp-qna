package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatedAtTest {
    @DisplayName("생성일자 생성")
    @Test
    void constructCreatedAt_success() {
        LocalDateTime now = LocalDateTime.now();
        CreatedAt createdAt = new CreatedAt(now);

        assertThat(createdAt).isEqualTo(new CreatedAt(now));
    }

    @DisplayName("잘못된 형식으로 생성일자 생성 시 에러")
    @Test
    void constructNullCreatedAt_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CreatedAt(null))
            .withMessage("비어있지 않은 생성일자를 입력해주세요.");
    }

    @DisplayName("전달받은 생성일자보다 이후이거나 같음")
    @Test
    void isAfterOrEqualTo_true() {
        LocalDateTime now = LocalDateTime.now();
        CreatedAt beforeCreatedAt = new CreatedAt(now);

        assertThat(new CreatedAt(now).isAfterOrEqualTo(beforeCreatedAt)).isTrue();
    }

    @DisplayName("전달받은 생성일자보다 이전")
    @Test
    void isAfterOrEqualTo_false() {
        LocalDateTime now = LocalDateTime.now();

        assertThat(new CreatedAt(LocalDateTime.MIN).isAfterOrEqualTo(new CreatedAt(now))).isFalse();
    }
}