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
    @DisplayName("userId로 User 데이터를 조회할 수 있다.")
    void findByUserId() {
        User user = userRepository.save(UserTest.JAVAJIGI);

        Optional<User> findUser = userRepository.findByUserId(user.getUserId());

        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get().getId()).isEqualTo(user.getId());
        assertThat(findUser.get().getUserId()).isNotNull();
        assertThat(findUser.get().getPassword()).isNotNull();
        assertThat(findUser.get().getName()).isNotNull();
    }
}