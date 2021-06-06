package qna.domain.wrappers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentIdTest {

    @Test
    void contentId_원시값_포장_객체_생성() {
        ContentId contentId = new ContentId(1L);
        assertThat(contentId).isEqualTo(new ContentId(1L));
    }
}
