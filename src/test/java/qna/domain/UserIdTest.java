package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

    @DisplayName("아이디는 영문, 숫자 조합으로 최대 20자리가 넘지 않아야 한다.")
    @Test
    void validateUserIdType() {
        //given
        String userId = "user1";

        //when
        UserId actual = new UserId(userId);

        //then
        assertThat(actual.getUserId()).isEqualTo(userId);
    }

    @Test
    void validateUserIdTypeException() {
        //when
        assertThatThrownBy(() -> new UserId("한글아이디하고싶어"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(UserId.INVALID_USER_ID_MESSAGE);
    }

    @Test
    void validateUserIdLengthException() {
        //when
        assertThatThrownBy(() -> new UserId("testtesttesttesttesttesttesttesttesttesttesttesttest"))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(UserId.INVALID_USER_ID_MESSAGE);
    }
}