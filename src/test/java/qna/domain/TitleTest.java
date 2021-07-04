package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @Test
    void create() {
        String validTitle = "IRON MAN";

        Title title = new Title(validTitle);
        assertThat(title.title()).isEqualTo(validTitle);
    }

    @Test
    void createInvalidTitle() {
        String invalidTitle = "0asdjaljksdlkj@asdjsadasdasdjakdlsjflasjdfkjaskldjfajsdfjasldjfjasdfja;lsdjfajskdlfjasjdfljas;kdjf;jalsjdsaljd@.aaa";

        assertThatThrownBy(() -> new Title(invalidTitle))
                .isInstanceOf(IllegalArgumentException.class);
    }

}