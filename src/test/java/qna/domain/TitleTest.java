package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TitleTest {
    @DisplayName("Title 생성")
    @Test
    void init() {
        String text = "title test";
        Title title = new Title(text);

        assertThat(title).isNotNull();
    }

    @DisplayName("Title 비교")
    @Test
    void equalToTitle() {
        String text = "title test";

        Title standard = new Title(text);
        Title target = new Title(text);

        assertThat(standard).isEqualTo(target);
    }

    @DisplayName("title 길이 제한 확인")
    @Test
    void invalidTitleLength() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                String text = IntStream.range(0, 200)
                    .boxed()
                    .map(String::valueOf)
                    .collect(Collectors.joining());

                Title title = new Title(text);
            });
    }
}
