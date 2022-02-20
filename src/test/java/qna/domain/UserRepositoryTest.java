package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        final User user = userRepository.save(UserTest.JAVAJIGI);
        assertThat(user).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("id 를 통해서 user 를 찾는다")
    void findByUserId() {
        final User user = userRepository.save(UserTest.JAVAJIGI);
        final Optional<User> findUser = userRepository.findByUserId(user.getUserId());
        assertThat(findUser.get().getId()).isEqualTo(user.getId());
    }
}