package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {
    @DisplayName("validate UserId")
    @Test
    void validateMatchIdException() {
        assertThatThrownBy(() -> {
            UserId userId = new UserId("wrongId");
            userId.validateMatchUserId(new UserId("userId"));
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("get UserId Max Length 예외")
    @Test
    void getUserIdMaxLengthException() {
        assertThatThrownBy(() -> {
            UserId userId = new UserId("User Id 20 글자 넘기기 20 글자 넘기기 20 글자 넘기기");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userId 최대입력 길이를 초과하였습니다.");
    }

}