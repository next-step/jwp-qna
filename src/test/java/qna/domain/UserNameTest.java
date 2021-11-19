package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserNameTest {
    @DisplayName("save UserName")
    @Test
    void getUserName() {
        // given, when
        UserName userName = new UserName("myUser");

        // then
        assertThat(userName.getName()).isEqualTo("myUser");
    }

    @DisplayName("get UserName Max Length 예외")
    @Test
    void getUserNameMaxLengthException() {
        assertThatThrownBy(() -> {
            // given, when
            UserName userId = new UserName("User Name 20 글자 넘기기 20 글자 넘기기 20 글자 넘기기");

            // then
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userName 최대입력 길이를 초과하였습니다.");
    }
}