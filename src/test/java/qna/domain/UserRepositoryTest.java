package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId() {
        User user = new User("userId", "비밀번호", "홍길동", "h@email.com");
        userRepository.save(user);

        Optional<User> actual = userRepository.findByUserId(user.getUserId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(user.getId());
    }
}