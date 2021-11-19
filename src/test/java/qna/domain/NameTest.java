package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import qna.domain.exception.IllegalNameException;
import qna.domain.exception.IllegalPasswordException;
import qna.domain.exception.NameLengthExceedException;
import qna.domain.exception.PasswordLengthExceedException;

class NameTest {

    @Test
    void test_이름이_null_이나_빈_문자열_예외() {
        assertAll(
            () -> assertThatThrownBy(() -> Name.from(null))
                .isInstanceOf(IllegalNameException.class),
            () -> assertThatThrownBy(() -> Name.from(""))
                .isInstanceOf(IllegalNameException.class)
        );
    }

    @Test
    void test_이름_길이제한_초과시_예외() {
        String longName = "김수한무거북이와두루미삼천갑자동박삭이하생략";

        assertThatThrownBy(() -> Name.from(longName))
            .isInstanceOf(NameLengthExceedException.class);
    }
}