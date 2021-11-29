package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자_저장_테스트() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        assertThat(user).isNotNull();
    }

    @Test
    void 사용자_조회_테스트() {
        userRepository.save(UserTest.JAVAJIGI);

        Optional<User> user = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId());

        assertThat(user).isNotEmpty();
        assertThat(user.get().getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
    }

    @Test
    void 사용자_삭제_테스트() {
        userRepository.save(UserTest.JAVAJIGI);
        assertThat(userRepository.findByUserId(UserTest.JAVAJIGI.getUserId())).isNotEmpty();

        userRepository.delete(UserTest.JAVAJIGI);

        assertThat(userRepository.findByUserId(UserTest.JAVAJIGI.getUserId())).isEmpty();
    }

    @Test
    void 객체_동일성_테스트() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get()).isEqualTo(user);
    }
}
