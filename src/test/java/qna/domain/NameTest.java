package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    @DisplayName("이름이 null 이면 예외를 던진다.")
    void fromException1() {
        Assertions.assertThatThrownBy(() -> Name.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이름이 20자 초과이면 예외를 던진다.")
    void fromException2() {
        Assertions.assertThatThrownBy(() -> Name.from("123456789012345678901"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals 테스트 (동일한 경우)")
    void equals1() {
        Name name1 = Name.from("user1");
        Name name2 = Name.from("user1");
        Assertions.assertThat(name1).isEqualTo(name2);
    }

    @Test
    @DisplayName("equals 테스트 (동일하지 않은 경우)")
    void equals2() {
        Name name1 = Name.from("user1");
        Name name2 = Name.from("user2");
        Assertions.assertThat(name1).isNotEqualTo(name2);
    }
}