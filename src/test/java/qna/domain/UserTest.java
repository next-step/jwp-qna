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
        assertThat(saved).isEqualTo(JAVAJIGI);
    }

    @Test
    void 사용자_조회() {
        User user = userRepository.save(JAVAJIGI);

        User expected = userRepository.findByUserId(user.getUserId()).get();
        assertThat(user).isEqualTo(expected);
    }

    @Test
    void 사용자_수정() {
        User user = new User("donghee.han", "password", "donghee", "donghee@slipp.net");
        userRepository.save(user);

        user.setName("haha");
        user.setEmail("haha@test.com");
        userRepository.save(user);

        User expected = userRepository.findByUserId(user.getUserId()).get();
        assertThat(user).isEqualTo(expected);
    }

    @Test
    void 사용자_삭제() {
        User user = new User("donghee.han", "password", "donghee", "donghee@slipp.net");
        userRepository.save(user);

        userRepository.delete(user);
        Optional<User> find = userRepository.findByUserId(user.getUserId());
        assertThat(find.isPresent()).isFalse();
    }
}
