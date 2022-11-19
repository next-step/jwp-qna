package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class NameTest {
    @Test
    void generate_name() {
        // given
        Name actual = Name.of("son");
        // when
        Name expect = Name.of("son");
        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void name_empty_exception() {
        // when // then
        assertThatThrownBy(() -> Name.of("")).isInstanceOf(IllegalArgumentException.class);
    }
}