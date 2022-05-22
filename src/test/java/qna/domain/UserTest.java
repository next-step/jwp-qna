package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    void 수정_테스트() {
        User user = new User(1L, "javajigi", "password", "name2", "javajigi2@slipp.net");
        user.update(user, JAVAJIGI);
        assertThat(user.getName()).isEqualTo(JAVAJIGI.getName());
        assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail());
    }

    @Test
    void 수정_불가() {
        User user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        assertThatThrownBy(() -> user.update(UserTest.SANJIGI, user)).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 저장_테스트() {
        User newUser = UserTest.JAVAJIGI;
        userRepository.save(newUser);
        User savedUser = userRepository.findByUserId("javajigi").get();
        assertThat(newUser.getUserId()).isEqualTo(savedUser.getUserId());
    }
}
