package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ContentsTest {

    @Test
    void 컨텐츠_생성() {
        //given
        Contents actual = Contents.of("컨텐츠");

        //when
        Contents expect = Contents.of("컨텐츠");

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 컨텐츠_toString() {
        //given
        Contents actual = Contents.of("컨텐츠");

        //then
        assertThat(actual.toString()).contains("Contents{contents=");
    }
}
