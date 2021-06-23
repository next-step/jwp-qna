package qna.domain;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

    @Test
    void create() {
        String validUserId = "ascii92der";
        UserId userId = new UserId(validUserId);

        assertThat(userId).extracting("userId").isEqualTo(validUserId);
    }

    @Test
    void createInvalidUserId() {
        String invalidUserId = "afsdaf@@@";

        assertThatThrownBy(() -> new UserId(invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void matchUserId() {
        String userIdString = "ascii92der";
        String anotherUserIdString = "ascii92";
        UserId userId = new UserId(userIdString);
        UserId anotherUserId = new UserId(anotherUserIdString);

        assertThatThrownBy(() -> userId.matchUserId(anotherUserId))
                .isInstanceOf(UnAuthorizedException.class);
    }
}