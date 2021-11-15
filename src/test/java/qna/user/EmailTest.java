package qna.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.TestStringGenerate.generateByLength;

class EmailTest {
    @ParameterizedTest
    @ValueSource(ints = {
            1, 50
    })
    @DisplayName("사용자 이메일 생성")
    public void createEmail(int emailLength) {
        String email = generateByLength(emailLength);
        Email actual = new Email(email);
        assertThat(actual).isEqualTo(new Email(email));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            51
    })
    @DisplayName("사용자 이메일 생성 - 실패(nullable = false)")
    public void createEmail_fail(int emailLength) {
        String email = generateByLength(emailLength);
        assertThatThrownBy(() -> new Email(email)).isInstanceOf(IllegalArgumentException.class);
    }
}