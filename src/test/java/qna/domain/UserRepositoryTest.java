package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByUserId() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        User user = userRepository.findByUserId(savedUser.getUserId()).get();

        assertThat(user).isNotNull();
    }

}