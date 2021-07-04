package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentsTest {

    @Test
    void create() {
        String content = "How can I code well?";

        Contents contents = new Contents(content);

        assertThat(contents.content()).isEqualTo(content);
    }
}