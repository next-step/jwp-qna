package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.constant.ErrorCode;

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

    @Test
    void 제목_길이가_길면_예외를_발생시킨다() {
        //given
        String actual = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123";

        //then
        assertThatThrownBy(() -> Title.of(actual)).isInstanceOf(IllegalArgumentException.class).hasMessage(ErrorCode.제목의_길이가_너무_김.getErrorMessage());
    }
}
