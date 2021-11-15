package qna.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.TestStringGenerate.generateByLength;

class NameTest {
    @ParameterizedTest
    @ValueSource(ints = {
            1, 20
    })
    @DisplayName("사용자 이름 생성")
    public void createName(int nameLength) {
        String name = generateByLength(nameLength);
        Name actual = new Name(name);
        assertThat(actual).isEqualTo(new Name(name));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 21
    })
    @DisplayName("사용자 이름 생성 - 실패")
    public void createName_fail(int nameLength) {
        String name = generateByLength(nameLength);
        assertThatThrownBy(() -> new Name(name)).isInstanceOf(IllegalArgumentException.class);
    }
}