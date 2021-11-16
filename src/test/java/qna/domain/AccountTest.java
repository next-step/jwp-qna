package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AccountTest {
    @ParameterizedTest
    @DisplayName("객체 생성 시, not null인 필드에 null이 전달될 경우 예외 발생")
    @MethodSource("provideParametersIncludingNull")
    void createByNull(String userId, String password) {
        assertThatNullPointerException().isThrownBy(() ->
            new Account(userId, password)
        );
    }

    private static Stream<Arguments> provideParametersIncludingNull() {
        return Stream.of(
            Arguments.of(null, "password"),
            Arguments.of("javajigi", null)
        );
    }
}
