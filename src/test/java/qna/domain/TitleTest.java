package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TitleTest {
    @Test
    void 제목_length_100_초과시_에러_테스트() {
        //given
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            stringBuilder.append("백");
        }

        //when && then
        assertAll(
                () -> assertThat(stringBuilder.length()).isEqualTo(101),
                () -> assertThatThrownBy(() -> new Title(stringBuilder.toString()))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 제목_빈값_또는_null_입력시_에러_테스트() {
        assertAll(
                () -> assertThatThrownBy(() -> new Title(null)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Title("")).isInstanceOf(IllegalArgumentException.class)
        );
    }
}