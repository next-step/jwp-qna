package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    private static final String TITLE1 = "title";

    @Test
    void createTitleWithValidInput() {
        Title title = new Title(TITLE1);

        assertThat(title.getTitle()).isEqualTo(TITLE1);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void createTitleWithInValidInput(String input) {
        assertThatThrownBy(() -> new Title(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
