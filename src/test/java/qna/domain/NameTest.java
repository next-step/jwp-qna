package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    void 이름_생성() {
        //given
        Name actual = Name.of("javajigi");

        //when
        Name expect = Name.of("javajigi");

        //then
        assertThat(actual).isEqualTo(expect);
        assertThat(actual.isEqualName(expect)).isTrue();
    }

    @Test
    void 이름_다름_테스트() {
        //given
        Name actual = Name.of("javajigi");

        //when
        Name expect = Name.of("sanjigi");

        //then
        assertThat(actual).isNotEqualTo(expect);
        assertThat(actual.isEqualName(expect)).isFalse();
    }

    @Test
    void 이름_toString() {
        //given
        String actual = "javajigi";
        Name name = Name.of(actual);

        //then
        assertThat(name.toString()).contains("Name{name='" + actual);
    }
}
