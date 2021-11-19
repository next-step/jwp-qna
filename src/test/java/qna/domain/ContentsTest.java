package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContentsTest {
    @DisplayName("Contents 생성")
    @Test
    void init() {
        String text = "Qna test";
        Contents contents = new Contents(text);

        String result = contents.getContents();

        assertThat(result).isEqualTo(text);
    }
}
