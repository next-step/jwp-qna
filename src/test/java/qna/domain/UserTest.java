package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @Test
     void save_사용자_생성() {
        User actual = new User("lsh", "lsh", "lsh", "lsh@gmail.com");
        User expected = users.save(actual);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByUserId_사용자_아이디_조회() {
        User actual = users.save(JAVAJIGI);
        User expected = users.findByUserId(actual.getUserId()).orElse(null);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void update_사용자_정보_수정() {
        User targetUser = new User("javajigi", "password", "lsh", "lsh@slipp.net");
        User actual = users.save(JAVAJIGI);
        actual.update(JAVAJIGI, targetUser);
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("lsh"),
                () -> assertThat(actual.getEmail()).isEqualTo("lsh@slipp.net")
        );
    }

    @Test
    void match_사용자_이름_이메일_조회() {
        User expected = users.save(JAVAJIGI);
        User target = new User("javajigi", "password", "name", "javajigi@slipp.net");
        assertThat(expected.equalsNameAndEmail(target)).isTrue();
    }

    @Test
    void guest_게스트_사용자() {
        User user = User.GUEST_USER;
        assertThat(user.isGuestUser()).isTrue();
    }
}
