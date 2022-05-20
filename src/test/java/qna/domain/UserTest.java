package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자_생성() {
        User saved = userRepository.save(JAVAJIGI);
        assertThat(saved).isNotNull();
    }

    @Test
    void 사용자_조회() {
        userRepository.save(JAVAJIGI);
        Optional<User> user = userRepository.findByUserId(JAVAJIGI.getUserId());
        assertThat(user.get()).isNotNull();
    }

    @Test
    void 사용자_수정() {
        User user = userRepository.save(new User("donghee.han", "password", "donghee", "donghee@slipp.net"));

        user.setName("han");
        user.setEmail("han@slipp.net");
        userRepository.save(user);

        User actual = userRepository.findByUserId(user.getUserId()).get();
        assertThat(actual.getName()).isEqualTo("han");
        assertThat(actual.getEmail()).isEqualTo("han@slipp.net");
    }

    @Test
    void 사용자_삭제() {
        User user = userRepository.save(new User("donghee.han", "password", "donghee", "donghee@slipp.net"));
        userRepository.delete(user);
        Optional<User> actual = userRepository.findByUserId(user.getUserId());
        assertThat(actual.isPresent()).isFalse();
    }
}
