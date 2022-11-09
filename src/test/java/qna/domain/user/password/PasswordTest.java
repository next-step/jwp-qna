package qna.domain.user.password;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PasswordTest {

    @DisplayName("제한 된 길이를 넘어가면 EX 발생")
    @ParameterizedTest
    @CsvSource(value = {"1:123456789123456789123"}, delimiter = ':')
    void length(String success, String ex) {
        assertThatNoException().isThrownBy(() -> new Password(success));
        assertThatIllegalArgumentException().isThrownBy(() -> new Password(ex));
    }

    @DisplayName("null 이거나 empty 이면 EX 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void nullAndEmpty(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Password(password));
    }
}
