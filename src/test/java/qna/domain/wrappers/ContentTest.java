package qna.domain.wrappers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentTest {

    @Test
    void contents_생성() {
        Contents contents = new Contents("content1");
        assertThat(contents).isEqualTo(new Contents("content1"));
    }
}
