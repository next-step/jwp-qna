package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Email 테스트")
class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"jordy@kakao.com", "scappy@daum.net"})
    void new_성공(String source) {
        assertDoesNotThrow(() -> new Email(source));
    }

    @ParameterizedTest
    @ValueSource(strings = {"jordy", "scappy"})
    void new_예외_유효하지_않은_이메일(String source) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email(source));
    }

    @DisplayName("new_예외_길이_제한_초과")
    @Test
    void new_예외_길이_제한_초과() {
        // Given
        StringBuilder sourceBuilder = new StringBuilder();
        for (int i = 0; i < Email.MAXIMUM_LENGTH; i++)
            sourceBuilder.append("jordy");
        sourceBuilder.append("@kakao.com");

        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email(sourceBuilder.toString()));
    }

    @DisplayName("equals_성공")
    @ParameterizedTest
    @ValueSource(strings = {"jordy@kakao.com", "scappy@daum.net"})
    void equals_성공(String source) {
        // Given
        Email email1 = new Email(source);
        Email email2 = new Email(source);

        // When, Then
        assertEquals(email1, email2);
    }
}
