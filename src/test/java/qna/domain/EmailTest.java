package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void create() {
        String validEmail = "asdf@gmail.com";
        Email email = new Email(validEmail);

        assertThat(email).extracting("email").isEqualTo(validEmail);
    }

    @Test
    void createInvalidEmail() {
        String invalidEmail = "0asdjaljksdlkj@asdjsadjalsjdsaljd@.aaa";

        assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class);
    }
}