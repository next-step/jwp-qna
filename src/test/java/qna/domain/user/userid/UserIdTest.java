package qna.domain.user.userid;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class UserIdTest {

    @DisplayName("제한 된 길이를 넘어가면 EX 발생")
    @ParameterizedTest
    @CsvSource(value = {"1:123456789123456789123"}, delimiter = ':')
    void length(String success, String ex) {
        assertThatNoException().isThrownBy(() -> new UserId(success));
        assertThatIllegalArgumentException().isThrownBy(() -> new UserId(ex));
    }

    @DisplayName("null 이거나 empty 이면 EX 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void nullAndEmpty(String userId) {
        assertThatIllegalArgumentException().isThrownBy(() -> new UserId(userId));
    }

}
