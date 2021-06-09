package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.Contents.INVALID_NULL_OR_EMPTY_CONTENTS_MESSAGE;

class ContentsTest {

    @Test
    void create() {
        //given
        String contents = "컨텐츠";

        //when
        Contents actual = new Contents(contents);

        //then
        assertThat(actual.getContents()).isEqualTo(contents);
    }

    @DisplayName("컨텐츠는 null이거나 빈문자열인 경우 예외를 발생시킨다.")
    @Test
    void nullOrEmptyContentsException() {
        //when
        assertThatThrownBy(() -> new Contents(null))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(INVALID_NULL_OR_EMPTY_CONTENTS_MESSAGE);
        assertThatThrownBy(() -> new Contents(""))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(INVALID_NULL_OR_EMPTY_CONTENTS_MESSAGE);
    }
}