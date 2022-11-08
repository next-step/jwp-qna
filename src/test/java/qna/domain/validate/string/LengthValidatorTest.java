package qna.domain.validate.string;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LengthValidatorTest {

    @ParameterizedTest
    @DisplayName("설정한 길이를 넘지 않으면 통과 넘으면 EX 발생")
    @CsvSource(value = {"4:123:12345", "5:abcd:abcdef"}, delimiter = ':')
    void length(int limitLength, String success, String ex) {
        StringValidator validator = new LengthValidator(limitLength);
        assertThatNoException().isThrownBy(() -> validator.validate(success));
        assertThatIllegalArgumentException().isThrownBy(() -> validator.validate(ex));
    }
}
