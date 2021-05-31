package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {
    private static final String EMAIL = "test@test.com";
    private static final String NOT_EMAIL = EMAIL + "_INVALID";

    @DisplayName("유효한 이메일로 이메일 객체 생성")
    @Test
    void createWithValidEmail() {
        Email email = new Email(EMAIL);

        assertThat(email.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("유효하지 않은 이메일로 이메일 객체 생성")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"test.com", "aaaaaaaaaabbbbbbbbbbcccccccccccccdddddddddddddeeeeeeee@test.com"})
    void createWithInValidEmail(String input) {
        assertThatThrownBy(() -> new Email(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
