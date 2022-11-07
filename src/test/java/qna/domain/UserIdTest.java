package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

class UserIdTest {

    @Test
    void 유저Id_생성() {
        //given
        UserId actual = UserId.of("javajigi");

        //when
        UserId expect = UserId.of("javajigi");

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 유저Id_다름_테스트() {
        //given
        UserId actual = UserId.of("javajigi");

        //when
        UserId expect = UserId.of("sanjigi");

        //then
        assertThat(actual).isNotEqualTo(expect);
    }

    @Test
    void 유저Id_toString() {
        //given
        String actual = "javajigi";
        UserId userId = UserId.of(actual);

        //then
        assertThat(userId.toString()).contains("UserId{userId='" + actual);
    }

    @Test
    void 서로_다른_유저_Id일_경우_예외를_발생시킨다() {
        //given
        UserId actual = UserId.of("javajigi");
        UserId expect = UserId.of("sanjigi");

        //when
        assertThatThrownBy(() -> actual.validateMatchUserId(expect)).isInstanceOf(UnAuthorizedException.class);
    }
}
