package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("본문 내용 테스트")
class ContentsTest {

    @DisplayName("생성 성공")
    @Test
    void createFrom_contents_success() {
        //given:
        final String content = "abc";
        final Contents contents = new Contents("abc");
        //when, then:
        assertThat(Contents.from(content)).isEqualTo(contents);
    }
}
