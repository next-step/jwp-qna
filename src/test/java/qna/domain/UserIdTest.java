package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {
    @Test
    void generate_userId() {
        // given
        UserId actual = UserId.of("son");
        // when
        UserId expect = UserId.of("son");
        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void password_match_test() {
        // given
        UserId actual = UserId.of("son");
        // when
        UserId expect = UserId.of("son");
        // then
        assertThat(actual.matchUserId(expect)).isTrue();
    }
}