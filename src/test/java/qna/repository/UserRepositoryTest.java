package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("user save() 테스트를 진행한다")
    void saveUser() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");

        User saveUser = userRepository.save(user);

        assertThat(saveUser).isEqualTo(user);
    }

    @Test
    @DisplayName("user가 삭제가 되는지 확인한다")
    void userDelete() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User saveUser = userRepository.save(user);
        userRepository.deleteById(saveUser.getId());

        Optional<User> result = userRepository.findById(saveUser.getId());

        assertThat(result).isEmpty();
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("user를 조회한다")
    void userFind() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User saveUser = userRepository.save(user);

        Optional<User> result = userRepository.findByUserId(saveUser.getUserId());


        assertThat(result).get().isEqualTo(saveUser);
    }
}