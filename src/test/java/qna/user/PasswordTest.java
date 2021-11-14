package qna.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.TestStringGenerate.generateByLength;

class PasswordTest {
    @ParameterizedTest
    @ValueSource(ints = {
            1, 20
    })
    @DisplayName("사용자 비밀번호 생성")
    public void createPassword(int passwordLength) {
        String password = generateByLength(passwordLength);
        Password actual = new Password(password);
        assertThat(actual).isEqualTo(new Password(password));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 21
    })
    @DisplayName("사용자 비밀번호 생성 - 실패")
    public void createPassword_fail(int passwordLength) {
        String password = generateByLength(passwordLength);
        assertThatThrownBy(() -> new Password(password)).isInstanceOf(IllegalArgumentException.class);
    }
}