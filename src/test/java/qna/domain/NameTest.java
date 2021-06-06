package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @DisplayName("이름은 영문, 한글, 숫자 조합으로 최대 20자리가 넘지 않아야 한다.")
    @Test
    void validateNameType() {
        //given
        String name = "앙리뒤프레2세";

        //when
        Name actual = new Name(name);

        //then
        assertThat(actual.getName()).isEqualTo(name);
    }

    @Test
    void validateNameTypeException() {
        //when
        assertThatThrownBy(() -> new Name("홍길동@"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Name.INVALID_NAME_MESSAGE);
    }

    @Test
    void validateNameLengthException() {
        //when
        assertThatThrownBy(() -> new Name("testtesttesttesttesttesttesttesttesttesttesttesttest"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Name.INVALID_NAME_MESSAGE);
    }
}