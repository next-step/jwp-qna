package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserPasswordTest {
    @DisplayName("validate user password")
    @Test
    void validateMatchPasswordException() {
        assertThatThrownBy(() -> {
            UserPassword userPassword = new UserPassword("wrongPassword");
            userPassword.validateMatchPassword(new UserPassword("password"));
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("get UserPassword Max Length 예외")
    @Test
    void getUserPasswordMaxLengthException() {
        assertThatThrownBy(() -> {
            UserPassword userPassword = new UserPassword("User Password 20 글자 넘기기 20 글자 넘기기 20 글자 넘기기");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userPassword 최대입력 길이를 초과하였습니다.");
    }

}