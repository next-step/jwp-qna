package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {
    private static final String USER_NAME = "user1";

    @Test
    void createNameWithValidInput() {
        Name name = new Name(USER_NAME);

        assertThat(name.getName()).isEqualTo(USER_NAME);
    }

    @ParameterizedTest
    @ValueSource(strings = "abcdefghijklmnopqrstuvabcdefghijklmnopqrstuv")
    @NullAndEmptySource
    void createNameWithInValidInput(String input) {
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
