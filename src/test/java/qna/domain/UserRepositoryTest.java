package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("DB의 사용자를 저장한 후 해당 ID로 조회할 수 있다.")
    void findByUserId_사용자_아이디_조회() {
        final User actual = users.save(JAVAJIGI);

        final User expected = users.findByUserId(actual.getUserId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("DB의 사용자를 저장한 후 일치하는 사용자라면 사용자 정보를 수정할 수 있다.")
    void update_사용자_정보_수정() {
        final User actual = users.save(JAVAJIGI);

        final User expected = new User("javajigi", "password", "lsh", "lsh@slipp.net");

        actual.update(JAVAJIGI, expected);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    @DisplayName("DB의 저장된 사용자 정보의 이름과 이메일이 같으면 True를 반환할수 있다.")
    void match_사용자_이름_이메일_조회() {
        final User expected = users.save(JAVAJIGI);

        final User target = new User("javajigi", "password", "name", "javajigi@slipp.net");

        assertThat(expected.equalsNameAndEmail(target)).isTrue();
    }

    @Test
    @DisplayName("사용자가 게스트 사용자인지 확인할 수 있다.")
    void guest_게스트_사용자() {
        final User user = User.GUEST_USER;

        assertThat(user.isGuestUser()).isTrue();
    }
}
