package qna.domain.validate.string;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class NullAndEmptyValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    void empty_null(String target) {
        StringValidator validator = new NullAndEmptyValidator();
        assertThatIllegalArgumentException().isThrownBy(() -> validator.validate(target));
    }

}
