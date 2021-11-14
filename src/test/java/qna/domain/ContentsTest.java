package qna.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContentsTest {
    @Test
    @DisplayName("잘못된 값이 들어갈때 오류 확인")
    void 데이터_확인() {
        assertThrows(IllegalArgumentException.class, () -> {Contents.of("");});
    }

}
