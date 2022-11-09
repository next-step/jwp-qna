package qna.domain.user.email;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmailTest {

    @DisplayName("제한 된 길이를 넘어가면 EX 발생")
    @ParameterizedTest
    @CsvSource(value = {"kym1026@kakao.com:123456789123456789123456789123456789123456789@kakao.com"}, delimiter = ':')
    void length(String success, String ex) {
        assertThatNoException().isThrownBy(() -> new Email(success));
        assertThatIllegalArgumentException().isThrownBy(() -> new Email(ex));
    }
}
