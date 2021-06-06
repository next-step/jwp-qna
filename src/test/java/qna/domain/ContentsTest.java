package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}