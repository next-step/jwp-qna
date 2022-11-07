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
    void 컨텐츠_다름_테스트() {
        //given
        Contents actual = Contents.of("컨텐츠");

        //when
        Contents expect = Contents.of("컨텐츠 다름");

        //then
        assertThat(actual).isNotEqualTo(expect);
    }

    @Test
    void 컨텐츠_toString() {
        //given
        String actual = "컨텐츠";
        Contents contents = Contents.of(actual);

        //then
        assertThat(contents.toString()).contains("Contents{contents='" + actual);
    }
}
