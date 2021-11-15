package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTest {

    @DisplayName("제목은 1자 이상 100자 이하로 작성해야 한다")
    @Test
    void create() {
        StringBuilder longText = new StringBuilder();
        IntStream.range(0, 56)
            .forEach(longText::append);

        assertThatThrownBy(() -> new Title(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Title(longText.toString()))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Title(""))
            .isInstanceOf(IllegalArgumentException.class);

        assertDoesNotThrow(() -> new Title("test"));
    }
}
