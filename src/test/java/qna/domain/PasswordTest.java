package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import qna.domain.exception.IllegalPasswordException;
import qna.domain.exception.PasswordLengthExceedException;

class PasswordTest {

    @Test
    void test_비밀번호가_null_이나_빈_문자열_예외() {
        assertAll(
            () -> assertThatThrownBy(() -> Password.from(null))
                .isInstanceOf(IllegalPasswordException.class),
            () -> assertThatThrownBy(() -> Password.from(""))
                .isInstanceOf(IllegalPasswordException.class)
        );
    }

    @Test
    void test_비밀번호_길이제한_초과시_예외() {
        String longPassword = "abcdefghijklmnopqrstuvwxyz";

        assertThatThrownBy(() -> Password.from(longPassword))
            .isInstanceOf(PasswordLengthExceedException.class);
    }

    @Test
    void test_동일_여부_확인() {
        Password password1 = Password.from("password");
        Password password2 = Password.from("password");
        Password password3 = Password.from("secret");

        assertAll(
            () -> assertThat(password1.match(password2)).isTrue(),
            () -> assertThat(password1.match(password3)).isFalse()
        );
    }
}