package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DataJpaTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @ParameterizedTest
    @NullAndEmptySource
    void user_id_null_테스트(String userId) {
        assertThatIllegalArgumentException().isThrownBy(() -> new User(1L, userId, "password", "name", "javajigi@slipp.net"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void password_null_테스트(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> new User(1L, "javajigi", password, "name", "javajigi@slipp.net"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void name_null_테스트(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> new User(1L, "javajigi", "password", name, "javajigi@slipp.net"));
    }

    @Test
    void 사용자_저장_테스트() {
        User user = userRepository.save(JAVAJIGI);

        assertThat(user).isNotNull();
    }

    @Test
    void 사용자_조회_테스트() {
        userRepository.save(JAVAJIGI);

        Optional<User> user = userRepository.findByUserId(JAVAJIGI.getUserId());

        assertThat(user).isNotEmpty();
        assertThat(user.get().getUserId()).isEqualTo(JAVAJIGI.getUserId());
    }

    @Test
    void 사용자_삭제_테스트() {
        userRepository.save(JAVAJIGI);
        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId())).isNotEmpty();

        userRepository.delete(JAVAJIGI);

        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId())).isEmpty();
    }

    @Test
    void 객체_동일성_테스트() {
        User user = userRepository.save(JAVAJIGI);

        assertThat(userRepository.findByUserId(JAVAJIGI.getUserId()).get()).isEqualTo(user);
    }
}
