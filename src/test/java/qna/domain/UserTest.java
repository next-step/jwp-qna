package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "JAVJIGI",
        "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "SANJIGI",
        "sanjigi@slipp.net");

    @Test
    @DisplayName("User update할때 id가 일치하지 않으면 예외를 발생시킨다.")
    void UnAuthorizedException_on_MatchUserIdTest() {
        User loginUser = new User(1L, "javajigi#", "igijavaj", "JAVJIGI", "javajigi@slipp.net");

        assertThatThrownBy(() -> JAVAJIGI.update(loginUser, SANJIGI))
            .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("User update할때 password가 일치하지 않으면 예외를 발생시킨다.")
    void UnAuthorizedException_on_matchPasswordTest() {
        User targetUser = new User(2L, "sanjigi", "igijnas#", "SANJIGI", "sanjigi@slipp.net");

        assertThatThrownBy(() -> SANJIGI.update(SANJIGI, targetUser))
            .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("유저 정보를 수정한다.")
    void updateTest() {
        JAVAJIGI.update(JAVAJIGI, SANJIGI);

        assertThat(JAVAJIGI.getName()).isEqualTo(SANJIGI.getName());
        assertThat(JAVAJIGI.getEmail()).isEqualTo(SANJIGI.getEmail());
    }

}
