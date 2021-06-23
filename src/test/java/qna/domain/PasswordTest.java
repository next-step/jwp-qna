package qna.domain;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @Test
    void create() {
        String validPassword = "!@#$%&*()";
        Password password = new Password(validPassword);

        assertThat(password).extracting("password").isEqualTo(validPassword);
    }

    @Test
    void createInvalidUserId() {
        String invalidUserId = "askasdlkflasdjflkasdjflkjasdlkfjlasdj";

        assertThatThrownBy(() -> new Password(invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void matchPassword() {
        String passwordString = "asdfqwer";
        String anotherPasswordString = "qwerasdf";

        Password password = new Password(passwordString);
        Password anotherPassword = new Password(anotherPasswordString);
        assertThatThrownBy(() -> password.matchPassword(anotherPassword))
                .isInstanceOf(UnAuthorizedException.class);
    }
}