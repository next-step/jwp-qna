package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TitleTest {
    @Test
    @DisplayName("잘못된 값이 들어갈때 오류 확인")
    void 데이터_확인() {
        // Given
        StringBuilder overSizeText = new StringBuilder();
        IntStream.range(0, Title.TITLE_MAX_SIZE+1).forEach(i -> overSizeText.append("a"));
        
        // When, Then
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> {Title.of("");}),
                () -> assertThrows(IllegalArgumentException.class, () -> {Title.of(overSizeText.toString());}));
    }
}
