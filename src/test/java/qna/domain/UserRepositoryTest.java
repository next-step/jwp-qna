package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("DB의 사용자를 저장한 후 해당 ID로 조회할 수 있다.")
    void findByUserId_사용자_아이디_조회() {
        final User actual = 사용자_저장("writer");

        final User expected = 사용자_조회(actual);

        assertAll(
                () -> assertThat(expected).isEqualTo(actual),
                () -> assertThat(expected.getUserId()).isEqualTo(actual.getUserId()),
                () -> assertThat(expected.getPassword()).isEqualTo(actual.getPassword()),
                () -> assertThat(expected.getEmail()).isEqualTo(actual.getEmail())
        );
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("DB의 저장된 사용자 정보의 이름과 이메일이 같으면 True를 반환할수 있다.")
    void match_사용자_이름_이메일_조회() {
        final User actual = 사용자_저장("writer");

        final User target = new User(actual.getUserId(), actual.getPassword(), actual.getName(), actual.getEmail());

        assertThat(actual.equalsNameAndEmail(target)).isTrue();
    }

    private User 사용자_저장(String writer) {
        return users.save(new User(writer, "password", "name", "lee@slipp.net"));
    }

    private User 사용자_조회(User actual) {
        return users.findByUserId(actual.getUserId()).orElseThrow(NotFoundException::new);
    }

    private void 사용자_수정(User actual, User expected) {
        actual.update(actual, expected);
    }
}
