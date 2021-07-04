package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @Test
    void create() {
        String validName = "asdasfkasdfas";
        Name name = new Name(validName);

        assertThat(name).extracting("name").isEqualTo(validName);
    }

    @Test
    void createInvalidEmail() {
        String invalidName = "aaaasdalsjfljsadlfjlasjdddlfk";

        assertThatThrownBy(() -> new Name(invalidName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}