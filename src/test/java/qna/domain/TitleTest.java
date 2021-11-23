package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTest {
    private static final String LENGTH_100_TEXT =
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

    @DisplayName("제목 생성")
    @Test
    void constructTitle_success() {
        Title title = new Title(LENGTH_100_TEXT);
        assertThat(title).isEqualTo(new Title(LENGTH_100_TEXT));
    }

    @DisplayName("잘못된 형식으로 제목 생성 시 에러")
    @Test
    void constructNullTitle_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Title(null))
            .withMessage("길이 100이하의 비어있지 않은 제목을 입력해주세요.");
    }

    @DisplayName("잘못된 형식으로 제목 생성 시 에러")
    @Test
    void constructTitleWithLength101_error() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Title(LENGTH_100_TEXT + 1))
            .withMessage("길이 100이하의 비어있지 않은 제목을 입력해주세요.");
    }
}