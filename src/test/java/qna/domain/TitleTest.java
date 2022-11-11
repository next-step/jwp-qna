package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("제목 테스트")
class TitleTest {

    @DisplayName("생성 성공")
    @Test
    void create_title_success() {
        //given:
        final String titleString = "title";
        Title title = new Title(titleString);
        assertThat(title.getName()).isEqualTo(titleString);
    }
}
