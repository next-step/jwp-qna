package qna.domain.validate.string;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class NullAndEmptyValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    void empty_null(String target) {
        NullAndEmptyValidator validator = NullAndEmptyValidator.getInstance();
        assertThatIllegalArgumentException().isThrownBy(() -> validator.validate(target));
    }

}
