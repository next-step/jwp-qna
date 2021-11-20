package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.domain.exception.EmailLengthExceedException;

class EmailTest {

    @Test
    void test_이메일_주소_길이_초과시_예외() {
        String longEmail = "abcdefghijklmnopqrstuvwxyz@abcdefghijklmnopqrstuvwxyz.com";

        assertThatThrownBy(() -> Email.from(longEmail))
            .isInstanceOf(EmailLengthExceedException.class);
    }
}