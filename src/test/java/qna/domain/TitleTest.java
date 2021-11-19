package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.Name.MAX_NAME_LENGTH;
import static qna.domain.Title.MAX_TITLE_LENGTH;
import static qna.util.TestUtils.createStringLongerThan;

class TitleTest {

    @Test
    @DisplayName("제목 정상 생성")
    void title_생성() {
        // given
        String title = "테스트";

        // when
        Title result = new Title(title);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("제목에 null이 입력될 경우 실패한다.")
    void title_null() {
        // when, then
        assertThatThrownBy(() -> new Title(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("제목은 반드시 입력되어야 합니다. (최대 길이: " + MAX_TITLE_LENGTH + ")");
    }

    @Test
    @DisplayName("제목의 길이가 최대 길이보다 긴 경우 생성 실패한다.")
    void title_길이_초과() {
        // given
        String title = createStringLongerThan(MAX_TITLE_LENGTH);

        // when, then
        assertThatThrownBy(() -> new Title(title))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("제목은 " + MAX_TITLE_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + title + ")");
    }
}