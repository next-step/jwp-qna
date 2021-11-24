package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {
    private static final String LENGTH_20_TEXT = "12345678901234567890";

    @DisplayName("이름 생성")
    @Test
    void constructName_success() {
        Name name = new Name(LENGTH_20_TEXT);
        assertThat(name).isEqualTo(new Name(LENGTH_20_TEXT));
    }

    @DisplayName("잘못된 형식으로 이름 생성 시 에러")
    @Test
    void constructNullName_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Name(null))
            .withMessage("길이 20이하의 비어있지 않은 이름을 입력해주세요.");
    }

    @DisplayName("잘못된 형식으로 이름 생성 시 에러")
    @Test
    void constructNameWithLength101_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Name(LENGTH_20_TEXT + 1))
            .withMessage("길이 20이하의 비어있지 않은 이름을 입력해주세요.");
    }
}