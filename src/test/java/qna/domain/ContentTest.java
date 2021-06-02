package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTest {
    private static final String CONTENT1 = "content1";

    @Test
    void create() {
        Contents contents = new Contents(CONTENT1);

        assertThat(contents.getContent()).isEqualTo(CONTENT1);
    }
}
