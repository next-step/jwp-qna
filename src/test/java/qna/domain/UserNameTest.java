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
        UserName userName = new UserName("myUser");

        assertThat(userName.getName()).isEqualTo("myUser");
    }

    @DisplayName("get UserName Max Length 예외")
    @Test
    void getUserNameMaxLengthException() {
        assertThatThrownBy(() -> {
            UserName userId = new UserName("User Name 20 글자 넘기기 20 글자 넘기기 20 글자 넘기기");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userName 최대입력 길이를 초과하였습니다.");
    }
}