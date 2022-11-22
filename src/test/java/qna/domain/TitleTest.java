package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TitleTest {
    static final String lengthTestString;

    static {
        String tempStr = "";
        for (int i = 0; i <= 100; i++) {
            tempStr += 1;
        }
        lengthTestString = tempStr;
    }

    @Test
    void generrate_title_test() {
        // given // when
        Title title = Title.of("title");
        // then
        assertThat(title.toString()).contains("title");
    }

    @Test
    void title_empty_test() {
        //when // then
        assertThatThrownBy(() -> Title.of("")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void title_length_over_test() {
        // then
        assertThatThrownBy(() -> Title.of(lengthTestString))
                .isInstanceOf(IllegalArgumentException.class);
    }
}