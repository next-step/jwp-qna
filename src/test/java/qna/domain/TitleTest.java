package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @Test
    void create() {
        //given
        String title = "타이틀";

        //when
        Title actual = new Title(title);

        //then
        assertThat(actual.getTitle()).isEqualTo(title);
    }

    @Test
    void validateTitleLengthException() {
        //given
        String title = "titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle";

        //when
        assertThatThrownBy(() -> new Title(title))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Title.INVALID_TITLE_MESSAGE);
    }
}