package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContentsTest {

    @Test
    void test_생성() {
        assertThat(Contents.from("내용입니다.")).isInstanceOf(Contents.class);
    }

    @Test
    void test_내용_확인() {
        Contents contents = Contents.from("내용입니다.");
        assertThat(contents.contents()).isEqualTo("내용입니다.");
    }

    @Test
    void test_equals_비교() {
        Contents contents1 = Contents.from("강남역");
        Contents contents2 = Contents.from("강남역");
        Contents contents3 = Contents.from("판교역");

        assertAll(
            () -> assertThat(contents1).isEqualTo(contents2),
            () -> assertThat(contents1).isNotEqualTo(contents3)
        );
    }
}