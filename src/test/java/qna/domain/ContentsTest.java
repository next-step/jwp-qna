package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ContentsTest {

    @Test
    void static_factory_method_test() {
        // given
        Contents actual = Contents.of("컨텐츠");
        // when
        Contents expect = Contents.of("컨텐츠");
        // then
        assertThat(actual).isEqualTo(expect);
    }
}