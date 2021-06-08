package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @DisplayName("이름은 영문, 한글, 숫자 조합으로 이루어져있다.")
    @Test
    void validateNameType() {
        //given
        String name = "앙리뒤프레2세";

        //when
        Name actual = new Name(name);

        //then
        assertThat(actual.getName()).isEqualTo(name);
    }

    @DisplayName("이름에 영문, 한글, 숫자가 아닌 다른 문자가 있다면 예외를 발생시킨다.")
    @Test
    void validateNameTypeException() {
        //when
        assertThatThrownBy(() -> new Name("홍길동@😀;!"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Name.INVALID_NAME_MESSAGE);
    }

    @DisplayName("이름 길이가 20자리 이상일 때 예외를 발생시킨다.")
    @Test
    void validateNameLengthException() {
        //when
        assertThatThrownBy(() -> new Name("testtesttesttesttesttesttesttesttesttesttesttesttest"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(Name.INVALID_NAME_MESSAGE);
    }
}