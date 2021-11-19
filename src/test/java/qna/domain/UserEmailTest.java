package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserEmailTest {
    @DisplayName("change UserEmail")
    @Test
    void changeUserEmail() {
        // given
        UserEmail userEmail = new UserEmail("email@test.com");

        // when
        userEmail.changeEmail("change@test.com");

        // then
        assertThat(userEmail.getEmail()).isEqualTo("change@test.com");
    }

    @DisplayName("get UserEmail Max Length 예외")
    @Test
    void getUserEmailMaxLengthException() {
        assertThatThrownBy(() -> {
            // given, when
            UserEmail userEmail = new UserEmail("user Email 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 " +
                    "50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 " +
                    "50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기 50 글자 넘기기");

            // then
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("user email 최대입력 길이를 초과하였습니다.");
    }
}