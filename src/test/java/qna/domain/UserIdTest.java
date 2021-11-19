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
            // given
            UserId userId = new UserId("wrongId");

            // when
            userId.validateMatchUserId(new UserId("userId"));

            // then
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("get UserId Max Length 예외")
    @Test
    void getUserIdMaxLengthException() {
        assertThatThrownBy(() -> {
            // given, when
            UserId userId = new UserId("User Id 20 글자 넘기기 20 글자 넘기기 20 글자 넘기기");

            // then
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userId 최대입력 길이를 초과하였습니다.");
    }

}