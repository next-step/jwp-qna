package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ContentsTest {
    @DisplayName("contents change")
    @Test
    void changeContents() {
        Contents contents = new Contents("origin contents");
        contents.changeContents("new contents");

        assertThat(contents.getContents()).isEqualTo("new contents");
    }
}