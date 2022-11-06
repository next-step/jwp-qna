package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TitleTest {

    @Test
    void 제목_생성() {
        //given
        Title actual = Title.of("제목");

        //when
        Title expect = Title.of("제목");

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 제목_다름_테스트() {
        //given
        Title actual = Title.of("제목");

        //when
        Title expect = Title.of("제목 다름");

        //then
        assertThat(actual).isNotEqualTo(expect);
    }

    @Test
    void 제목_toString() {
        //given
        String actual = "제목";
        Title title = Title.of(actual);

        //then
        assertThat(title.toString()).contains("Title{title='" + actual);
    }
}
