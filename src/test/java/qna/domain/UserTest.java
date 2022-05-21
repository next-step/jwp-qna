package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    void 사용자_정보를_변경할_수_있어야_한다() {
        // given
        final User user = new User(0L, "ttungga", "password", "old name", "old email");
        final String newName = "new name";
        final String newEmail = "new email";

        // when
        final User updatedUser = new User(0L, "ttungga", "password", newName, newEmail);
        user.update(user, updatedUser);

        // then
        assertThat(user.getName()).isEqualTo(newName);
        assertThat(user.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void 특정_사용자가_다른_사용자와_이름과_이메일_정보가_같은지_확인할_수_있어야_한다() {
        // given
        final String name = "name";
        final String email = "email";
        final User user = new User(0L, "ttungga", "password", name, email);
        final User same = new User(0L, "same", "password1", name, email);
        final User differentAll = new User(0L, "different", "password2", "da", "da");
        final User differentParticularly = new User(0L, "different", "password2", "df", email);

        // when and then
        assertThat(user.equalsNameAndEmail(same)).isTrue();
        assertThat(user.equalsNameAndEmail(differentAll)).isFalse();
        assertThat(user.equalsNameAndEmail(differentParticularly)).isFalse();
    }
}
