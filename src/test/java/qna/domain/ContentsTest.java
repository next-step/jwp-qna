package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ContentsTest {
    @DisplayName("contents change")
    @Test
    void changeContents() {
        // given
        Contents contents = new Contents("origin contents");

        // when
        contents.changeContents("new contents");

        // then
        assertThat(contents.getContents()).isEqualTo("new contents");
    }
}