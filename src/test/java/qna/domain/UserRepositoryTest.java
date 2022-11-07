package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository users;

    @Test
    @DisplayName("User 저장 테스트")
    void save_user_test() {
        User JAVAJIGI = UserTest.JAVAJIGI;
        User savedUser = users.save(JAVAJIGI);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(JAVAJIGI.getUserId());
    }

    @Test
    @DisplayName("UserId로 User 조회 테스트")
    void find_by_user_id_test() {
        User JAVAJIGI = UserTest.JAVAJIGI;
        users.save(JAVAJIGI);
        Optional<User> findUser = users.findByUserId(JAVAJIGI.getUserId());
        assertThat(findUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("ID가 같은 두 클래스의 일치 테스트")
    void identity_test() {
        final User user1 = users.save(new User("test", "test1", "테스트", "test@test.com"));
        final User user2 = users.findById(user1.getId()).get();
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("이름 및 이메일 업데이트 테스트")
    void name_and_email_update_test() {
        final User JAVAJIGI = users.save(UserTest.JAVAJIGI);
        User target = new User(JAVAJIGI.getUserId(), JAVAJIGI.getPassword(), "HAPPY", "HAPPY@test.com");
        JAVAJIGI.update(JAVAJIGI, target);
        assertThat(users.findByUserId(JAVAJIGI.getUserId()).get().getEmail()).isEqualTo("HAPPY@test.com");
    }

    @Test
    @DisplayName("일치하지 않은 비밀번호로 업데이트 테스트")
    void update_with_invalid_password_test() {
        final User JAVAJIGI = users.save(UserTest.JAVAJIGI);
        User target = new User(JAVAJIGI.getUserId(), "test", "HAPPY", "HAPPY@test.com");

        assertThatThrownBy(() ->
                JAVAJIGI.update(JAVAJIGI, target)
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("이름과 이메일 일치 확인 테스트")
    void equals_name_and_email_test() {
        final User JAVAJIGI = users.save(UserTest.JAVAJIGI);
        User target = new User();
        target.setName(JAVAJIGI.getName());
        target.setEmail(JAVAJIGI.getEmail());
        assertThat(JAVAJIGI.equalsNameAndEmail(target)).isTrue();
    }

    @Test
    @DisplayName("이름과 이메일 일치 확인 테스트")
    void check_not_identical_name_test() {
        final User JAVAJIGI = users.save(UserTest.JAVAJIGI);
        User target = new User();
        target.setName("test");
        target.setEmail(JAVAJIGI.getEmail());
        assertThat(JAVAJIGI.equalsNameAndEmail(target)).isFalse();
    }

    @Test
    @DisplayName("게스트 유저가 아닌 경우 테스트")
    void check_not_guest_user_test() {
        final User JAVAJIGI = users.save(UserTest.JAVAJIGI);
        assertThat(JAVAJIGI.isGuestUser()).isFalse();
    }

    @Test
    @DisplayName("게스트 유저 확인 테스트")
    void check_guest_user_test() {
        final User JAVAJIGI = users.save(UserTest.JAVAJIGI);
        final User guestUeser = JAVAJIGI.GUEST_USER;
        assertThat(guestUeser.isGuestUser()).isTrue();
    }


}
